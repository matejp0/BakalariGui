package cz.matejprerovsky.bakalarigui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;

import org.json.*;
import static jdk.nashorn.internal.objects.NativeString.trim;

public class Bakal {
    private static boolean connected;
    final private String[] dayOfWeek = new String[]{"Po", "Út", "Stř", "Čt", "Pá"};//new String[]{"Pondělí", "Úterý", "Středa", "Čtvrtek", "Pátek"};

    private final String baseURL;
    private URL targetURL;
    private String data;
    private String got;
    private String accessToken;
    private String refreshToken;
    private String[] baseSubjectAbbrev;

    //private boolean connected;
    public Bakal(String baseURL){
        this.baseURL=baseURL;
    }

    public boolean login(String username, String password, boolean refresh) throws IOException {
        if(!refresh) {
            data = "client_id=ANDR&grant_type=password&username=" + username + "&password=" + password;
        }
        else if(refresh){
            data = "client_id=ANDR&grant_type=refresh_token&refresh_token="+refreshToken;
        }
        targetURL=new URL(baseURL+"/api/login");

        //-----login--------------------------------------------------
        try{ got=this.request(targetURL, "POST", data, null); } catch (IOException ignored){}

        //-----Process JSON output---------------------------
        try {
            JSONObject obj = new JSONObject(got);
            accessToken = obj.getString("access_token");
            refreshToken = obj.getString("refresh_token");
        } catch (NullPointerException ignored){}

        return (got != null);
    }

    public String getUserInfo() throws IOException {
        targetURL=new URL(baseURL+"/api/3/user");
        got=this.request(targetURL, "GET", null, accessToken);

        //-----Process JSON output-------------------------
        JSONObject obj = new JSONObject(got);

        return obj.getString("FullName");
    }

    public String getMarks() throws IOException {
        String output = "";
        targetURL=new URL(baseURL+"/api/3/marks");
        got=this.request(targetURL, "GET", null, accessToken);
        JSONObject obj = new JSONObject(got);

        JSONArray subjects = obj.getJSONArray("Subjects");
        for(int i=0; i<subjects.length(); i++) {
            JSONObject subject = subjects.getJSONObject(i);

            String averageText = trim(subject.get("AverageText").toString());

            //-----Get subject abbrevation----------
            JSONObject subjectInfo = subject.getJSONObject("Subject");
            baseSubjectAbbrev = new String[subjects.length()];
            baseSubjectAbbrev[i] = trim(subjectInfo.getString("Abbrev"));

            //-----Get marks-------------------------
            JSONArray marks = subject.getJSONArray("Marks");
            String marksString = "";
            for (int j = 0; j < marks.length(); j++) {
                JSONObject mark = marks.getJSONObject(j);
                String markText = mark.getString("MarkText");
                String markCaption = mark.get("Caption").toString();
                int weight = mark.getInt("Weight");
                String date = this.getDate(mark.get("EditDate").toString());
                marksString += "\t" + markText + " (";
                if (markCaption.length() != 0)
                    marksString += markCaption + ", ";
                marksString += date + "), " + "Váha: " + weight + "\n";

            }
            //---------------------------------------------
            String result = "";
            result += baseSubjectAbbrev[i];
            //if (!averageText.equals("null") || averageText.length() != 0)
                result += " (" + averageText + ")";
            result += ":\n" + marksString;
            System.out.println(result);
            output += result;
        }
        return output;
    }

    public String[][] getTimetable(int day, int month, int year) throws IOException {
        String[][] arr = new String[6][13];
        targetURL=new URL(baseURL+"/api/3/timetable/actual?date="+year+"-"+month+"-"+day);
        got=this.request(targetURL, "GET", null, accessToken);
        JSONObject obj = new JSONObject(got);

        //-----Hours--------------------------------------
        JSONArray hours = obj.getJSONArray("Hours");
        String[] hourTimes = new String[hours.length()];
        for(int i=0; i<hours.length(); i++){
            JSONObject hour=hours.getJSONObject(i);
            String BeginTime = hour.getString("BeginTime");
            //String EndTime = hour.getString("EndTime");
            hourTimes[i]=" (" + BeginTime + ")"/*" - " + EndTime+")"*/;
        }
        //------------------------

        //-----Rooms-----------------------------------------
        JSONArray rooms = obj.getJSONArray("Rooms");
        String[] roomIds = new String[rooms.length()];
        String[] roomAbbrevs = new String[rooms.length()];
        for(int b=0; b<rooms.length(); b++){
            JSONObject room = rooms.getJSONObject(b);
            roomIds[b] = room.get("Id").toString();
            roomAbbrevs[b] = room.get("Abbrev").toString();
        }

        //------------------------

        //-----Subjects----------------------------------------
        JSONArray subjects = obj.getJSONArray("Subjects");
        baseSubjectAbbrev = new String[subjects.length() + 1];
        int[] baseSubjectId = new int[subjects.length() + 1];
        baseSubjectId[0]=0;
        baseSubjectAbbrev[0]="";
        for(int a=0; a<subjects.length(); a++){
            JSONObject sub = subjects.getJSONObject(a);
            baseSubjectAbbrev[a+1]=sub.getString("Abbrev");
            baseSubjectId[a+1] = Integer.parseInt(trim(sub.get("Id").toString()));
        }
        //-------------------

        //-----Days---------------------------------------------------------------------------
        JSONArray days = obj.getJSONArray("Days");
        for(int i=0; i<(days.length()); i++){
            JSONObject den = days.getJSONObject(i);

            //-----Day of week and Date-----------------------------
            String dateString=den.getString("Date");
            String dayOfWeekString = dayOfWeek[(den.getInt("DayOfWeek")-1)];
            arr[i+1][0]+=dayOfWeekString + " " + getDate(dateString);
            arr[i+1][0] = arr[i+1][0].substring(4);
            //----------------------------------

            //-----Lessons--------------------------------------------------
            JSONArray atoms = den.getJSONArray("Atoms");
            for(int j=0; j<atoms.length(); j++){
                JSONObject lesson = atoms.getJSONObject(j);

                int hourId = lesson.getInt("HourId");

                //-----Get room-------------------------
                String roomId = lesson.getString("RoomId");
                int indexOfRoom = 0;
                for(int c = 0; c< roomIds.length; c++){
                    if(roomId.equals(roomIds[c]))
                        indexOfRoom=c;
                }
                String roomAbbrev = roomAbbrevs[indexOfRoom];
                //-------------------------

                //-----Get changes in timetable-----
                JSONObject changeIs;
                String changeDescription="";
                String change = lesson.get("Change").toString();
                if (!change.equals("null")) {
                    changeIs = lesson.getJSONObject("Change");
                    changeDescription = changeIs.get("Description").toString();
                }
                //-----------------------------------------------

                //-----Get subject id and find its abbreviation-----
                String subjectAbbrev="";
                
                String subjectIdString = trim(lesson.get("SubjectId"));
                int subjectId = Integer.parseInt(subjectIdString);

                int indexOfSubject = 0;
                for (int k = 0; k < baseSubjectId.length; k++) {
                    if (subjectId == baseSubjectId[k]) {
                        indexOfSubject = k;
                    }
                    subjectAbbrev = baseSubjectAbbrev[indexOfSubject];

                }
                //-----------------------------------------------
                //---Get theme of lesson---
                //String theme = lesson.get("Theme").toString();
                //---Print result---
                String result=/*"\n"+" " + (hourId-2) + ": " + */subjectAbbrev/* + " " +(hourTimes[hourId-2])*/;
                //if(!theme.equals(""))
                    //result+=" | " + theme;
                
                result+=" " + roomAbbrev;
                //---If there is some change in timetable, print it---
                if (!changeDescription.equals(""))
                    result+=" (" + changeDescription + ")";

                arr[i+1][hourId-2+1] = result;

                for(int s=0; s<12; s++){
                    arr[0][s+1] = s +(hourTimes[s]);
                }
            }
        }
        return arr;
    }

    public static boolean connected(){
        connected = false;
        try {
            URL url = new URL("http://www.google.com");
            URLConnection connection = url.openConnection();
            connection.connect();
            connected = true;
        } catch (IOException e) {
            connected = false;
        }
        return connected;
    }
    private String request(URL target, String method, String data, String token) throws IOException {
        connected();
        //clear output
        String output = "";

        if(connected) {
            //-----Http request--------------------------------------------------
            HttpURLConnection conn = (HttpURLConnection) target.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            if (token != null) {
                conn.setRequestProperty("Authorization", "Bearer " + token);
            }
            conn.setRequestMethod(method);
            conn.setUseCaches(false);
            if (data != null) {
                conn.setRequestProperty("Content-Length", "" + data.length());
                try (OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream())) {
                    out.write(data);
                }
            }

            //-----Read input stream-------------------------------------
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String currentLine;
                while ((currentLine = in.readLine()) != null) {
                    output += currentLine;
                }
            } }
        return output;
    }
    private String getDate(String dateString){
        String[] dateInt=dateString.split("-");
        dateInt[2]=dateInt[2].substring(0, 2);

        //remove leading zeros
        dateInt[2]=dateInt[2].replaceFirst("^0+(?!$)", "");
        dateInt[1]=dateInt[1].replaceFirst("^0+(?!$)", "");

        return dateInt[2] + ". " + dateInt[1];
    }


}

