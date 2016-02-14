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



  
    
    public JAXBConfig(){
        
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
            System.out.println("Save file was not specified correctly");
        }
    }

    private JAXBContext createJAXBContext (Class<?> docClass) throws JAXBException {
        packageName = docClass.getPackage().getName();
        JAXBContext jc = JAXBContext.newInstance(packageName);
        return jc;
    }
}
