package com.kuhar.kos.tvspored;

import android.widget.ExpandableListView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import okhttp3.OkHttpClient;

/**
 * Created by Mark on 28. 12. 2016.
 */

public class XMLParse {

    private ArrayList<String> channelNames = new ArrayList();
    private ArrayList<String> channelLinks = new ArrayList();

    private ArrayList<ProgrammeData> currentShow  = new ArrayList();

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    String currDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    String currDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    private ArrayList<ProgrammeData> seznamOddaj = new ArrayList();

    private ArrayList<String> channelMeta = new ArrayList();

    private OkHttpClient client = new OkHttpClient();

    private API a = new API();

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

    private void parseCurrentShow (){
        try{
            for(int i = 0; i < channelLinks.size(); i++) {
                String xml = a.GET(client, channelLinks.get(i));

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput(new StringReader(xml));

                String startTime, endTime, startTimeTemp = "", endTimeTemp = "";

                boolean isTITLE = false,
                        existance = false;
                int eventType = xpp.getEventType();
                ProgrammeData pdCurr = new ProgrammeData();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    pdCurr = new ProgrammeData();
                    String sortNumber = "";
                    if (eventType == XmlPullParser.START_TAG) {
                        String tagName = xpp.getName();
                        if (tagName.equalsIgnoreCase("programme")) {
                            sortNumber = xpp.getAttributeValue(null, "SORT_NUMBER");
                            startTime = xpp.getAttributeValue(null, "BILLEDSTART");
                            endTime = xpp.getAttributeValue(null, "BILLEDEND");
                            SimpleDateFormat sdfCompare = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            if(validateDate(currDate + " " + startTime, currDateTime, currDate + " " + endTime)){
                                //pdCurr.title = channelLinks.get(i);
                                startTimeTemp = startTime;
                                endTimeTemp = endTime;
                                existance = true;
                            }
                        } else if (tagName.equalsIgnoreCase("title") && existance) {
                            isTITLE = true;
                        } else if(tagName.equalsIgnoreCase("slo") && existance) {
                            if (isTITLE) {
                                pdCurr.title = xpp.nextText();
                                isTITLE = false;
                                break;
                            }
                        }
                    }
                    eventType = xpp.next();
                }
                if(!existance){
                    ProgrammeData pdTemp = new ProgrammeData();
                    pdTemp.notFound();
                    currentShow.add(pdTemp);
                } else{
                    pdCurr.startTime = startTimeTemp;
                    pdCurr.endTime = endTimeTemp;
                    currentShow.add(pdCurr);
                    existance = false;
                }
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

            boolean isTITLE = false,
                    isSYNOPSIS_OPIS = false;

            int eventType = xpp.getEventType();
            ProgrammeData pd = new ProgrammeData();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                String sortNumber = "";
                if(eventType == XmlPullParser.START_TAG) {
                    String tagName = xpp.getName();
                    if (tagName.equalsIgnoreCase("programme")) {
                        seznamOddaj.add(pd);
                        pd = new ProgrammeData();
                        sortNumber = xpp.getAttributeValue(null, "SORT_NUMBER");
                        pd.sortNumber = sortNumber;
                        pd.startTime = xpp.getAttributeValue(null, "BILLEDSTART");
                        pd.endTime = xpp.getAttributeValue(null, "BILLEDEND");
                    } else if (tagName.equalsIgnoreCase("title")) {
                        isTITLE = true;
                    } else if (tagName.equalsIgnoreCase("SYNOPSIS_OPIS")) {
                        isSYNOPSIS_OPIS = true;
                    } else if(tagName.equalsIgnoreCase("slo")){
                        if (isTITLE){
                            pd.title = xpp.nextText();
                            isTITLE = false;
                        } else if (isSYNOPSIS_OPIS){
                            pd.description = xpp.nextText();
                            isSYNOPSIS_OPIS = false;
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

    private static boolean validateDate(String startDate, String currDate, String endDate)
    {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date1 = dateFormat.parse(startDate);
            Date date3 = dateFormat.parse(currDate);
            Date date2 = dateFormat.parse(endDate);
            if(date3.before(date2) && date3.after(date1))
            {
                return true;
            }
        }catch(Exception e)
        {
            System.out.println(e.getMessage());
        }

        return false;
    }

    public ArrayList<ProgrammeData> getSeznamOddaj(String url) {
        parseChannel(url);
        seznamOddaj.remove(0);
        return seznamOddaj;
    }

    public ArrayList<String> getChannelNames() {
        parseChannels();
        return channelNames;
    }

    public ArrayList<String> getChannelLinks() {
        return channelLinks;
    }

    public ArrayList<ProgrammeData> getCurrentShow() {
        parseCurrentShow();
        return currentShow;
    }
}
