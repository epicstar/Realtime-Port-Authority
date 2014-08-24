package rectangledbmi.com.pittsburghrealtimetracker.handlers;

import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import rectangledbmi.com.pittsburghrealtimetracker.world.Bus;

public class RequestTask extends AsyncTask<Void, Void, List<Bus>> {

    GoogleMap mMap;
    List<Bus> bl;
    String selectedBuses;

    public RequestTask(GoogleMap map, List<String> buses){
        mMap = map;
        selectedBuses = selectBuses(buses);
        bl = null;
    }

    /**
     * Takes the list of buses and returns a comma delimited string of routes
     * @param buses list of buses from the main activity thread
     * @return comma delimited string of buses. ex: buses -> [P1, P3]; return "P1,P3"
     */
    private String selectBuses(List<String> buses) {
        StringBuffer string = new StringBuffer();
        int oneLess = buses.size()-1;
        for(int i=0;i<buses.size();++i) {
            string.append(buses.get(i));
            if(i != oneLess) {
                string.append(",");
            }
        }
        return string.toString();
    }

    @Override
    protected List<Bus> doInBackground(Void... void1) {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = null;
        try {
            sp = spf.newSAXParser();
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            URL url = null;
            try {
                url = new URL(
                        "http://realtime.portauthority.org/bustime/api/v2/getvehicles?key=KiJEdJUDgRFxcG7cpt3ae6xxJ&rt=" + selectedBuses
                );
            } catch (MalformedURLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            SAXHandler handler;
            try {
                handler = new SAXHandler();
                try {
                    if (sp != null) {
                        sp.parse(new InputSource(url != null ? url.openStream() : null), handler);
                    }

                } catch (SAXException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                bl = handler.busList;
            } catch (NullPointerException sax) {
//                System.out.println(sax.getMessage());
                System.err
                        .println("Bus route is not tracked or all buses on route are in garage: " + selectedBuses);
//                System.exit(0);
            }
        return bl;
    }
    //TODO make a HashMap for the bus markers and use that to update the marker location...
    protected void onProgressUpdate(Integer... progress) {
    }

    @Override
    protected void onPostExecute(List<Bus> list) {
/*        <string name="title_section1">41</string>
        <string name="title_section2">48</string>
        <string name="title_section3">56</string>
        <string name="title_section4">8</string>
        <string name="title_section5">86</string>
        <string name="title_section6">88</string>
        <string name="title_section7">P1</string>
        <string name="title_section8">P3</string>*/
        if(bl != null) {
            for (Bus bus : bl) {
                LatLng latlng = new LatLng(bus.getLat(), bus.getLon());
                //TODO make the arrow the same name as the bus route to avoid (same as bus.getRt()) to avoid if else statements
                //TODO add the arrows to the git
                MarkerOptions marker = new MarkerOptions()
                        .position(latlng)
                        .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                        .snippet("Speed: " + bus.getSpd())
                        .draggable(false)
                        .rotation(bus.getHdg())
                        .icon(BitmapDescriptorFactory.fromAsset("arrow"))
                        .flat(true);
                mMap.addMarker(marker);
                //TODO use this one below when you change the arrow names to the bus.getRt()
    /*            MarkerOptions marker = new MarkerOptions()
                        .position(latlng)
                        .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                        .snippet("Speed: " + bus.getSpd())
                        .draggable(false)
                        .rotation(bus.getHdg())
                        .icon(BitmapDescriptorFactory.fromAsset("arrow"))
                        .flat(true);
                mMap.addMarker(marker);*/
    /*            if(bus.getRt().equals("41")) {
                    MarkerOptions marker = new MarkerOptions()
                    .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowBlack"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("48")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowBlue"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("56")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowGreen"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("8")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowOrange"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("86")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowPink"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("88")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowRed"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("P1")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowWhite"))
                            .flat(true);
                    mMap.addMarker(marker);
                }
                else if(bus.getRt().equals("P3")) {
                    MarkerOptions marker = new MarkerOptions()
                            .position(latlng)
                            .title(bus.getRt() + "(" + bus.getVid() + ") " + bus.getDes())
                            .snippet("Speed: " + bus.getSpd())
                            .draggable(false)
                            .rotation(bus.getHdg())
                            .icon(BitmapDescriptorFactory.fromAsset("arrowYellow"))
                            .flat(true);
                    mMap.addMarker(marker);
                }*/
            }
        }
    }
}