package teamtim.teamtimapp.network;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class NetworkSerialization {

    private NetworkSerialization() {

    }

    public static Map<String, String> fromRawData(byte[] buffer, int numBytes) {
        Map<String, String> data = new HashMap<>();

        // TODO: The source says it's unsupported?! But I can't find the StringFactory class..?
        String stringData = new String(buffer, 0, numBytes, Charset.forName("UTF-8"));

        // TODO: Make it a bit more safe maybe? Probably.
        String[] keyValuePairs = stringData.split(";");
        for (String pair: keyValuePairs) {
            String[] s = pair.split("=");
            data.put(s[0], s[1]);
        }

        return data;
    }

    public static byte[] asByteArray(Map<String, String> data) {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String stringEntry = entry.getKey() + "=" + entry.getValue() + ";";
            builder.append(stringEntry);
        }

        String stringData = builder.toString();
        return stringData.getBytes(Charset.forName("UTF-8"));
    }

}
