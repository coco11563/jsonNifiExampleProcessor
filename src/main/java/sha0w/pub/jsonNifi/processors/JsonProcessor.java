package sha0w.pub.jsonNifi.processors;

import com.jayway.jsonpath.JsonPath;
import org.apache.commons.io.IOUtils;
import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.components.PropertyDescriptor;
import org.apache.nifi.flowfile.FlowFile;
import org.apache.nifi.processor.*;
import org.apache.nifi.processor.exception.ProcessException;
import org.apache.nifi.processor.io.InputStreamCallback;
import org.apache.nifi.processor.io.OutputStreamCallback;
import org.apache.nifi.processor.util.StandardValidators;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

import static java.lang.System.in;

/**
 * Created by coco1 on 2017/7/18.
 */
@SideEffectFree
@Tags({"JSON","SHA0W.PUB"})
@CapabilityDescription("Fetch value from json path.")
public class JsonProcessor extends AbstractProcessor{

    private List<PropertyDescriptor> properties;
    private Set<Relationship> relationships;

    public static final String MATCH_ATTR = "match";

    public static final PropertyDescriptor JSON_PATH = new PropertyDescriptor.Builder()
            .name("Json Path")
            .required(true)
            .addValidator(StandardValidators.NON_EMPTY_VALIDATOR)
            .build();

    public static final Relationship SUCCESS = new Relationship.Builder()
            .name("SUCCESS")
            .description("Succes relationship")
            .build();
    @Override
    public void init(final ProcessorInitializationContext context ) {
        ArrayList<PropertyDescriptor> properties = new ArrayList<>();
        properties.add(JSON_PATH);
//        防止多线程ADD
        this.properties = Collections.unmodifiableList(properties);
        Set<Relationship> relationships = new HashSet<>();
        relationships.add(SUCCESS);
//        防止多线程ADD
        this.relationships = Collections.unmodifiableSet(relationships);
    }

    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        final AtomicReference<String> value = new AtomicReference<>();

        FlowFile flowFile = processSession.get();

        processSession.read(flowFile, in -> {
            try{
                String json = IOUtils.toString(in);
                String result = JsonPath.read(json, "$.hello");
                value.set(result);
            }catch(Exception ex){
                ex.printStackTrace();
                getLogger().error("Failed to read json string.");
            }
        });

        String results = value.get();
        if(results != null && !results.isEmpty()){
            flowFile = processSession.putAttribute(flowFile, "match", results);
        }

        // To write the results back out ot flow file
        flowFile = processSession.write(flowFile, out -> out.write(value.get().getBytes()));

        processSession.transfer(flowFile, SUCCESS);

    }

    @Override
    public Set<Relationship> getRelationships(){
        return relationships;
    }

    @Override
    public List<PropertyDescriptor> getSupportedPropertyDescriptors(){
        return properties;
    }
}
