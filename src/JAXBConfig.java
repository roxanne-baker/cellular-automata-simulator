// This entire file is part of my masterpiece.
// Sarp Uner

/*
 * This is the code I wrote for marshalling and unmarshalling XML files. I included this class as part of my masterpiece as it shows how important my design
 * decision to change the XML API was. Professor Duvall had told us that it is better to make a complete change to a part that doesn't show good design decisions 
 * than to preserve that part in fear of breaking the program. At the time that I told my team mates that I would completely change the way the program handled 
 * the configuration segment, they voiced objections as they were unsure of how safe and sound this new version would be. They suggested that, even though the old
 * design was not ideal, it worked, so I should stick with it. I disagreed, and I saved a lot of time, energy and code. The jaxbconfiguration package, which does 
 * the real hard work related to the XML files, is automatically generated using this API. Had I agreed to the idea of keeping the older XML API (which is the 
 * Configuration.java class, it is still available but is not used), things would have become very ugly for writing to XML files. I have also slightly modified the
 * code so that there are no more global variables. Also, the old constructor was removed, as it did not do anything specific to the class, and using no 
 * constructor, which by default calls Java's default constructor for classes without constructors, removed those few lines of code.
 */

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.xml.sax.InputSource;
import jaxbconfiguration.*;

public class JAXBConfig {
 
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
        return JAXBContext.newInstance(docClass.getPackage().getName());
    }
}
