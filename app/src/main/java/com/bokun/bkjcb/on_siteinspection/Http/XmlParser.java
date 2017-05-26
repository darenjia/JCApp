package com.bokun.bkjcb.on_siteinspection.Http;

import com.bokun.bkjcb.on_siteinspection.Utils.SoapManager;

import org.ksoap2.serialization.SoapObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

/**
 * Created by DengShuai on 2017/5/25.
 */

public class XmlParser {

    public static String parseJSON(String str) {
        XmlPullParserFactory factory = null;
        String res = "";
        try {
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(str));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_DOCUMENT) {

                } else if (eventType == XmlPullParser.START_TAG) {
                    String nodeName = xpp.getName().trim();
                    if ("GetUserResult".equals(nodeName)) { // employee节点
                        res = xpp.nextText();
                    } else if ("GetJianChaJiHuaResult".equals(nodeName)) { // employee节点
                        res = xpp.nextText();
                    } else if ("GetXxclScResult".equals(nodeName)) { // employee节点
                        res = xpp.nextText();
                    }
                } else if (eventType == XmlPullParser.END_TAG) {

                } else if (eventType == XmlPullParser.TEXT) {

                }
                eventType = xpp.next();
            }
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String parseSoapObject(SoapObject object) {
        SoapManager manager = SoapManager.getInstance();
        return manager.soapToJson(object);
    }
}
