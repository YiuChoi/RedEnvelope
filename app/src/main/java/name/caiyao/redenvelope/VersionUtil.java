package name.caiyao.redenvelope;

public class VersionUtil {

    public static String receiveUIFunctionName = "c";
    public static String getNetworkByModelMethod = "vP";
    public static String receiveUIParamName = "com.tencent.mm.v.k";
    public static String getMessageClass = "com.tencent.mm.e.b.bu";
    public static String QQPluginClass = "com.tenpay.android.qqplugin.a.p";
    public static void init(String version) {
        switch (version) {
            case "6.5.8":
                QQPluginClass = "com.tenpay.android.qqplugin.a.p";
                break;
            case "6.6.0":
                QQPluginClass = "com.tenpay.android.qqplugin.a.q";
                break;
            default:
                QQPluginClass = "com.tenpay.android.qqplugin.a.q";
        }
    }


    public static void init1(String version) {
        switch (version) {
            case "6.3.23":
                receiveUIFunctionName = "d";
                getNetworkByModelMethod = "vE";
                receiveUIParamName = "com.tencent.mm.t.j";
                getMessageClass = "com.tencent.mm.e.b.bl";
                break;
            case "6.3.25":
                receiveUIFunctionName = "d";
                getNetworkByModelMethod = "vF";
                receiveUIParamName = "com.tencent.mm.t.j";
                getMessageClass = "com.tencent.mm.e.b.bl";
                break;
            case "6.3.27":
                receiveUIFunctionName = "e";
                getNetworkByModelMethod = "yj";
                receiveUIParamName = "com.tencent.mm.u.k";
                getMessageClass = "com.tencent.mm.e.b.br";
                break;
            case "6.3.28":
                receiveUIFunctionName = "c";
                getNetworkByModelMethod = "vP";
                receiveUIParamName = "com.tencent.mm.v.k";
                getMessageClass = "com.tencent.mm.e.b.bu";
                break;
            case "6.3.30":
                receiveUIFunctionName = "c";
                getNetworkByModelMethod = "vS";
                receiveUIParamName = "com.tencent.mm.v.k";
                getMessageClass = "com.tencent.mm.e.b.bv";
                break;
            default:
                receiveUIFunctionName = "c";
                getNetworkByModelMethod = "vS";
                receiveUIParamName = "com.tencent.mm.v.k";
                getMessageClass = "com.tencent.mm.e.b.bv";
        }
    }
}
