package name.caiyao.redenvelope;

public class VersionUtil {

    public static String receiveUIFunctionName = "c";
    public static String getNetworkByModelMethod = "vP";
    public static String receiveUIParamName = "com.tencent.mm.v.k";
    public static String getMessageClass = "com.tencent.mm.e.b.bu";
    public static String RedPacketDetailsViewHolderClass = "nfa";
    public static String gameType = "cv";
    public static void init(String version) {
        switch (version) {
			case "6.2.0":
                RedPacketDetailsViewHolderClass = "jla";
                break;
			case "6.2.1":
                RedPacketDetailsViewHolderClass = "jir";
                break;
			case "6.2.3":
                RedPacketDetailsViewHolderClass = "jjv";
                break;
			case "6.3.1":
                RedPacketDetailsViewHolderClass = "kwy";
                break;
			case "6.3.3":
                RedPacketDetailsViewHolderClass = "kzo";
                break;
			case "6.3.5":
                RedPacketDetailsViewHolderClass = "mca";
                break;
			case "6.3.6":
                RedPacketDetailsViewHolderClass = "mca";
                break;
	        case "6.3.7":
                RedPacketDetailsViewHolderClass = "mbw";
                break;
            case "6.5.0":
                RedPacketDetailsViewHolderClass = "mjv";
                break;
            case "6.5.3":
                RedPacketDetailsViewHolderClass = "mkh";
                break;
            case "6.5.5":
                RedPacketDetailsViewHolderClass = "nnv";
                break;
            case "6.5.8":
                RedPacketDetailsViewHolderClass = "nfa";
                break;
            default:
                RedPacketDetailsViewHolderClass = "nfa";
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
