package controler;

import model.*;
import model.dtoModel.CurveDTO;
import model.dtoModel.Project;
import model.viewModel.MainWindowModel;

import javax.imageio.ImageIO;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Albert on 16.04.2016.
 */
public class XmlHelper {

    private static final XmlHelper instance = new XmlHelper();


    private XmlHelper() {

    }

    public static XmlHelper getInstance() {
        return instance;
    }

    public void marshal(File f, MainWindowModel model) {

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Project p = new Project();

            p.setBeziereCounter(BeziereCurve.count.get());
            p.setChainCounter(PolygonalChain.count.get());
            p.setRationalCounter(RationalBeziereCurve.count.get());
            p.setInterpolatedCounter(NewtonInterpolated.count.get());

            if (model.getBackground() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                try {
                    ImageIO.write(model.getBackground(), "png", baos);
                    baos.flush();
                    p.setEncodedBackGround(Base64.getEncoder().encodeToString(baos.toByteArray()));
                    baos.close();
                } catch (IOException e) {
                    System.err.printf("Fail to serialize image d %s", e.getMessage());
                }
            }


            List<CurveDTO> curves = new LinkedList<>();
            for (int i = 0; i < model.getCurveModel().getSize(); ++i) {
                curves.add(CurveDTO.toDTO(model.getCurveModel().getElementAt(i)));
            }
            p.setCurves(curves);

            jaxbMarshaller.marshal(p, f);
            jaxbMarshaller.marshal(p, System.out);
        } catch (JAXBException e) {
            //e.printStackTrace();
            System.err.printf("Save failed %s", e.getMessage());
        }

    }

    public void unmarshal(File f, MainWindowModel model) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Project.class);
            Unmarshaller jaxbMarshaller = jaxbContext.createUnmarshaller();
            Project p = (Project) jaxbMarshaller.unmarshal(f);
            model.getCurveModel().removeAllElements();
            BeziereCurve.count.set(p.getBeziereCounter());
            PolygonalChain.count.set(p.getChainCounter());
            RationalBeziereCurve.count.set(p.getRationalCounter());
            NewtonInterpolated.count.set(p.getInterpolatedCounter());

            for (CurveDTO curve : p.getCurves()) {
                ICurve c = CurveDTO.fromDTO(curve);
                model.getCurveModel().addElement(c);
                CurveUpdater.update(c, model.isDirty());
            }
            if (!p.getEncodedBackGround().isEmpty()) {
                ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(p.getEncodedBackGround()));
                try {
                    model.setBackground(ImageIO.read(bais));
                    bais.close();
                } catch (IOException e) {
                    System.err.printf("Fail to deserialize image d %s", e.getMessage());
                }

            }
            model.isDirty().compareAndSet(false, true);
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
