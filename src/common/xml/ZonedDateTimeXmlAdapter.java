package common.xml;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.ZonedDateTime;

/**
 * XmlAdapter needed to parse and unparse the creation date
 */
public class ZonedDateTimeXmlAdapter extends XmlAdapter<String, ZonedDateTime> {

    @Override
    public ZonedDateTime unmarshal(String v) throws Exception {
        return ZonedDateTime.parse(v.replace(" ", ""));
    }

    @Override
    public String marshal(ZonedDateTime v) throws Exception {
        return v.toString();
    }
}