import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import jaxbconfiguration.*;

public class JAXBConfig {
    
    private static String packageName;


/*
    public static void main (String[] args) {
        ObjectFactory objFact = new ObjectFactory();
        Jaxbconfiguration jaxbconfig = objFact.createJaxbconfiguration();
        JAXBConfig jxb = new JAXBConfig("test.xml");
        Class<?> cls = null;
        try {
            cls = Class.forName("jaxbconfiguration.Jaxbconfiguration");
        }
        catch (ClassNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        InputSource inp = new InputSource("src/resources/test.xml");
        try {
            jaxbconfig = jxb.unmarshal(cls, inp);
        }
        catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        System.out.println();
    } */
  
    
    public JAXBConfig(String file){
        
    }
    
    public Jaxbconfiguration unmarshal(Class<?> docClass, InputSource inputSource) throws JAXBException{
        Unmarshaller u = createJAXBContext(docClass).createUnmarshaller();
        Jaxbconfiguration doc = (Jaxbconfiguration) u.unmarshal(inputSource);
        return doc;
        }
    
    public void marshal(Class<?> docClass, Jaxbconfiguration jax) throws JAXBException{
        Marshaller m = createJAXBContext(docClass).createMarshaller();
        try {
            m.marshal(jax, new FileOutputStream("src/resources/saved_result.xml"));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace(); //bad!
        }
    }

    private JAXBContext createJAXBContext (Class<?> docClass) throws JAXBException {
        packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        return jc;
    }
}
