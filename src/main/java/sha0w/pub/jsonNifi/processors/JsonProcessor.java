package sha0w.pub.jsonNifi.processors;

import org.apache.nifi.annotation.behavior.SideEffectFree;
import org.apache.nifi.annotation.documentation.CapabilityDescription;
import org.apache.nifi.annotation.documentation.Tags;
import org.apache.nifi.processor.AbstractProcessor;
import org.apache.nifi.processor.ProcessContext;
import org.apache.nifi.processor.ProcessSession;
import org.apache.nifi.processor.exception.ProcessException;

/**
 * Created by coco1 on 2017/7/18.
 */
@SideEffectFree
@Tags({"JSON","SHA0W.PUB"})
@CapabilityDescription("Fetch value from json path.")
public class JsonProcessor extends AbstractProcessor{
    @Override
    public void onTrigger(ProcessContext processContext, ProcessSession processSession) throws ProcessException {
        
    }
}
