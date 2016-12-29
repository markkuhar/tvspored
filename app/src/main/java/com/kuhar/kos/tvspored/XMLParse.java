package com.kuhar.kos.tvspored;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.OkHttpClient;

/**
 * Created by Mark on 28. 12. 2016.
 */

public class XMLParse {

    ArrayList<String> channelNames = new ArrayList();
    ArrayList<String> channelLinks = new ArrayList();
    ArrayList<String> channelContent = new ArrayList();

    ArrayList<String> channelMeta = new ArrayList();

    OkHttpClient client = new OkHttpClient();

    API a = new API();
    private void parseChannels(){
        try {

            String xml = a.GET(client, "http://api.rtvslo.si/spored/listProvys");

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));
            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    String name = xpp.getName();
                    if (name.equalsIgnoreCase("schedule")) {
                        String chName = xpp.getAttributeValue(null, "title");
                        channelNames.add(chName);
                    } else if (name.equalsIgnoreCase("file")){
                        String fileDate = xpp.getAttributeValue(null, "date");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String currDate = sdf.format(new Date());
                        if (fileDate.equals(currDate)){
                            channelLinks.add(xpp.getAttributeValue(null, "name"));
                        }
                    }
                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
    private void parseChannel(String url){
        try {
            String xml = a.GET(client, url);

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            XmlPullParser xpp = factory.newPullParser();
            xpp.setInput(new StringReader(xml));

            boolean isTitle = false;
            boolean SYNOPSIS_OPIS = false;

            int eventType = xpp.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if(eventType == XmlPullParser.START_TAG) {
                    String name = xpp.getName();
                    if (name.equalsIgnoreCase("title")) {
                        isTitle = true;
                    } else if (name.equalsIgnoreCase("SYNOPSIS_OPIS")) {
                        SYNOPSIS_OPIS = true;
                    } else if(name.equalsIgnoreCase("slo")){
                        if (isTitle){
                            channelContent.add(xpp.nextText());
                            isTitle = false;
                        } else if (SYNOPSIS_OPIS){
                            channelMeta.add(xpp.nextText());
                            SYNOPSIS_OPIS = false;
                        }
                    }
                }

                eventType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getChannelContent(String url) {
        parseChannel(url);
        return channelContent;
    }

    public ArrayList<String> getChannelNames() {
        parseChannels();
        return channelNames;
    }

    public ArrayList<String> getChannelMeta(String url) {
        parseChannel(url);
        return channelMeta;
    }

    public ArrayList<String> getChannelLinks() {
        parseChannels();
        return channelLinks;
    }
}
