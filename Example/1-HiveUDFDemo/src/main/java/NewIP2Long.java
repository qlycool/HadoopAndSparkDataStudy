/**
 * Created by chuguangming on 16/7/26.
 */

import org.apache.hadoop.hive.ql.exec.UDF;

public class NewIP2Long extends UDF {
    public static long ip2long(String ip) {

        String[] ips = ip.split("[.]");
        long ipNum = 0;
        if (ips == null) {
            return 0;
        }
        for (int i = 0; i < ips.length; i++) {
            ipNum = ipNum << Byte.SIZE | Long.parseLong(ips[i]);
        }

        return ipNum;
    }

    public long evaluate(String ip) {
        if (ip.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            try {
                long ipNum = ip2long(ip);
                return ipNum;
            } catch (Exception e) {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public static void main(String[] argvs) {
        NewIP2Long ipl = new NewIP2Long();
        System.out.println(ip2long("112.64.106.238"));
        System.out.println(ipl.evaluate("58.35.186.62"));
    }
}
