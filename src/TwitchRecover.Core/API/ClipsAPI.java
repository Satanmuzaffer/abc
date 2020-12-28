/*
 * Copyright (c) 2020 Daylam Tayari <daylam@tayari.gg>
 *
 * This library is free software. You can redistribute it and/or modify it under the terms of the GNU General Public License version 3 as published by the Free Software Foundation.
 * This program is distributed in the that it will be use, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not see http://www.gnu.org/licenses/ or write to the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

/*
 * @author Daylam Tayari https://github.com/daylamtayari
 * @version 2.0
 * Github project home page: https://github.com/TwitchRecover
 * Twitch Recover repository: https://github.com/TwitchRecover/TwitchRecover
 */

package TwitchRecover.Core.API;

import TwitchRecover.Core.Enums.FileExtension;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This class handles all
 * of the API methods directly
 * related to clips.
 */
public class ClipsAPI {
    /**
     * This method returns the
     * permanent clip link of a
     * clip from a given slug.
     * @param slug      String value representing the clip's slug.
     * @return String   String value representing the permanent link of the clip.
     */
    public static String getClipLink(String slug){
        String response="";
        //API Query:
        try{
            CloseableHttpClient httpClient= HttpClients.createDefault();
            HttpGet httpget=new HttpGet("https://api.twitch.tv/kraken/clips/"+slug);
            httpget.addHeader("User-Agent", "Mozilla/5.0");
            httpget.addHeader("Accept", "application/vnd.twitchtv.v5+json");
            httpget.addHeader("Client-ID", "ohroxg880bxrq1izlrinohrz3k4vy6");
            CloseableHttpResponse httpResponse=httpClient.execute(httpget);
            if(httpResponse.getStatusLine().getStatusCode()==200){
                BufferedReader br=new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                String line;
                while((line=br.readLine())!=null){
                    response+=line;
                }
                br.close();
            }
        }
        catch(Exception ignored){}
        //Parse JSON:
        JSONParser parse=new JSONParser();
        JSONObject jObj=null;
        try{
            jObj=(JSONObject) parse.parse(response);
        }
        catch(ParseException ignored){}
        String streamID=jObj.get("broadcast_id").toString();
        JSONArray vod=(JSONArray) jObj.get("vod");
        int offset=Integer.parseInt(vod.get(2).toString())+26;
        return "https://clips-media-assets2.twitch.tv/"+streamID+"-offset-"+offset+ FileExtension.MP4;
    }
}