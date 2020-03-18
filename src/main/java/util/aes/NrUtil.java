package util.aes;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyuan on 2018/8/13.
 * main方法体中是多通道开发
 * <p>
 * 
 */
public class NrUtil {
    private static NrUtil nrUtil;

    private NrUtil() {
    }

    public static NrUtil getInstace() {
        if (nrUtil == null) {
            synchronized (NrUtil.class) {
                if (nrUtil == null) {
                    nrUtil = new NrUtil();
                }
            }
        }
        return nrUtil;
    }

    /**
     * read dat file(both)
     *
     * @return dat content
     * @throws IOException
     */
    public String readDatFile(String absoluteDatPath) {
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(absoluteDatPath);
            StringWriter stringWriter = new StringWriter();

            int len = 1;
            byte[] temp = new byte[len];
            
            /*16进制转化模块*/
            for (; (fileInputStream.read(temp, 0, len)) != -1; ) {
                if (temp[0] > 0xf && temp[0] <= 0xff) {
                    stringWriter.write(Integer.toHexString(temp[0]));
                } else if (temp[0] >= 0x0 && temp[0] <= 0xf) {//对于只有1位的16进制数前边补“0”
                    stringWriter.write("0" + Integer.toHexString(temp[0]));
                } else { //对于int<0的位转化为16进制的特殊处理，因为Java没有Unsigned int，所以这个int可能为负数
                    stringWriter.write(Integer.toHexString(temp[0]).substring(6));
                }
            }
            return stringWriter.toString();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 单通道读取dat文件中的Y值
     * Single(hex2decimal)
     * @param sHexContent
     * @param a
     * @param b
     * @return
     */
    public List<Float> SdatLast4H2D(String sHexContent, float a, float b) {
        List<Float> indexList = new ArrayList<Float>();
        int sHexContentLen = sHexContent.length();
        for (int i = 0; i < sHexContentLen; i += 20) {
            String hexStr = sHexContent.substring(i + 16, i + 20);
            char[] chars = hexStr.toCharArray();

            StringBuilder sb = new StringBuilder();
            sb.append(chars[2]);
            sb.append(chars[3]);
            sb.append(chars[0]);
            sb.append(chars[1]);
            short s = (short)(Integer.valueOf(sb.toString(), 16) & 0xffff);
            indexList.add(s * a + b);
        }
        return indexList;
    }

    /**
     * 读取dat文件中的Y值,暂时只判断了通道数1和2，再多的话，要添加判断语句
     * 
     * @param channelNum
     * @param sHexContent
     * @param a
     * @param b
     * @return
     */
    public List<Object> datH2D(int channelNum, String sHexContent, float a, float b) {
        int rowStringNum = 20 + 4 * (channelNum - 1);
        int hexContentLen = sHexContent.length();
//        int hexContentLen = 96;// test line

        List<Object> allList = new ArrayList<>();


        if (channelNum == 1) {
            List<Float> channelList1 = new ArrayList<>();
            List<Float> channelList2 = new ArrayList<>();
            for (int i = 0; i < hexContentLen; i += rowStringNum) {
                int A = i + rowStringNum;
                List<Float> list = new ArrayList<>();
//                for(int j = 1; j<= channelNum;j++){
                String hexStr = sHexContent.substring(A - 4, A);
                char[] chars = hexStr.toCharArray();
                StringBuilder sb = new StringBuilder();
                sb.append(chars[2]);
                sb.append(chars[3]);
                sb.append(chars[0]);
                sb.append(chars[1]);
                A = A - 4;
                short s = (short)(Integer.valueOf(sb.toString(), 16) & 0xffff);
                list.add(s* a + b);
//                }

                channelList1.add(list.get(0));

            }
            allList.add(channelList1);
            allList.add(channelList2);
            List<Integer> indexList = new ArrayList<>();
            for (int i = 0; i < channelList1.size(); i++) {
                indexList.add(i);
            }
            allList.add(indexList);
            return allList;

        } else if (channelNum == 2) {
            List<Float> channelList1 = new ArrayList<>();
            List<Float> channelList2 = new ArrayList<>();
            System.out.println(hexContentLen);
            
            for (int i = 0; i < hexContentLen; i += rowStringNum) {
            //for (int i = 0; i < hexContentLen-rowStringNum; i += rowStringNum) {
                int A = i + rowStringNum;
                List<Float> list = new ArrayList<>();
                for (int j = 1; j <= channelNum; j++) {
                    String hexStr = sHexContent.substring(A - 4, A);
                    char[] chars = hexStr.toCharArray();
                    StringBuilder sb = new StringBuilder();
                    sb.append(chars[2]);
                    sb.append(chars[3]);
                    sb.append(chars[0]);
                    sb.append(chars[1]);

                    A = A - 4;
                    
                    
                    //int six =Integer.parseInt(sb.toString(), 16);
                    short s = (short)(Integer.valueOf(sb.toString(), 16) & 0xffff);
                    list.add( s * a + b);
                }

                channelList1.add(list.get(0));
                channelList2.add(list.get(1));
            }
            allList.add(channelList1);
            allList.add(channelList2);

            //X轴的值
            List<Integer> indexList = new ArrayList<>();
            for (int i = 0; i < channelList1.size(); i++) {
                indexList.add(i);
            }
            allList.add(indexList);
            return allList;
        }else {
            return null;
        }
    }

    /**
     * 读取dat文件中的Y值
     * 根据入参可以修改读取点的个数
     * @param channelNum 
     * @param sHexContent
     * @param sIndex 开始点
     * @param eIndex 结束点
     * @return 
     * @author Zhengzy
     */
    public List<Object> getDatByI(int channelNum, String sHexContent, float a, float b,int sIndex,int eIndex) {
        System.out.println(channelNum);
        int rowStringNum = 20 + 4 * (channelNum - 1);
        int hexContentLen = sHexContent.length();
        List<Object> allList = new ArrayList<>();
        int sIh = sIndex*24;
        int eIh = eIndex*24;
        //if(sIh>eIh||eIh>hexContentLen||sIh<0)return null;
        //该方法只针对双通道
        if (channelNum == 2) {
            
            List<Float> channelList1 = new ArrayList<>();
            //List<Float> channelList2 = new ArrayList<>();
            for (int i = sIh; i < eIh; i += rowStringNum) {
                int A = i + rowStringNum;
                List<Float> list = new ArrayList<>();
                //16进制转十进制
                for (int j = 1; j <= channelNum; j++) {
                    String hexStr = sHexContent.substring(A - 4, A);
                    char[] chars = hexStr.toCharArray();
                    StringBuilder sb = new StringBuilder();
                    sb.append(chars[2]);
                    sb.append(chars[3]);
                    sb.append(chars[0]);
                    sb.append(chars[1]);
                    A = A - 4;
                    short s = (short)(Integer.valueOf(sb.toString(), 16) & 0xffff);
                    list.add( s * a + b);
                }
                
                channelList1.add(list.get(0));
                //channelList2.add(list.get(1));
            }
            allList.add(channelList1);
            //allList.add(channelList2);

            //X轴的值
            List<Integer> indexList = new ArrayList<>();
            for (int i = 0; i < channelList1.size(); i++) {
                indexList.add(i+sIndex);
            }
            allList.add(indexList);
            return allList;
        }else if(channelNum == 1){
            return datH2D(channelNum, sHexContent, a, b);
        }else{
            return null;
        }
    }

    /**
     * 读取cfg文件内容参数
     *
     * 还需要返回一个时间刻度，利用samp_Hz值
     *
     * {a1=1.200000, b1=0.300000, res=success, TT_channelNum=1, samp_Hz=10000000.000000, TT_channelNum1=1, time=13/5/2018,10:22:32.100101}
     * {a1=1.000000, b1=0.000000, res=success, TT_channelNum=2, samp_Hz=10000000.000000, TT_channelNum2=2, TT_channelNum1=2, time=24/5/2018,21:42:02.009239234, a2=1.000000, b2=0.000000}
     *
     * @param absoluteCFGPath
     * @return
     */
    public Map<String, String> ReadCFGAndGetParams(String absoluteCFGPath) {
        File file = new File(absoluteCFGPath);
        Map<String, String> cfgMap = new HashMap<String, String>();
        String cfgContent = null;
        char[] chars = new char[1024];
        try {
            InputStreamReader inputStreamReader = new InputStreamReader(new FileInputStream(file));
            while (true) {
                int charsLen = inputStreamReader.read(chars);
                if (charsLen == -1) {
                    break;
                }
                cfgContent = new String(chars, 0, charsLen);
            }
            if (cfgContent.length() == 0 || cfgContent.isEmpty()) {
                cfgMap.put("res", "null");
                return cfgMap;
            }
            String[] strings = cfgContent.replaceAll("\\r", "").replaceAll("\\n", ",").split(",");
            int TT_channelNum = Integer.parseInt(strings[3]);
            for (int i = 1; i < TT_channelNum + 1; i++) {
                cfgMap.put("TT_channelNum" + i, String.valueOf(TT_channelNum));
                cfgMap.put("a" + i, strings[11 + (i - 1) * 13]);
                cfgMap.put("b" + i, strings[12 + (i - 1) * 13]);
                cfgMap.put("df" + i, strings[13 + (i - 1) * 13]);
            }

            cfgMap.put("samp_Hz", strings[11 + (TT_channelNum - 1) * 13 + 10]);
            cfgMap.put("time", strings[11 + (TT_channelNum - 1) * 13 + 12] + "," + strings[11 + (TT_channelNum - 1) * 13 + 13]);
            cfgMap.put("TT_channelNum", String.valueOf(TT_channelNum));
            cfgMap.put("res", "success");


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return cfgMap;
    }

    /**
     * @param absoluteCFGPath .cfg或者.hdr的文件全路径
     * @return fileContent
     */
    public String ReadHDR(String absoluteCFGPath) {
        File file = new File(absoluteCFGPath);
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;
        String str = null;
        try {
            fileInputStream = new FileInputStream(file);
            inputStreamReader = new InputStreamReader(fileInputStream);
            char[] chars = new char[1024];
            while (true) {
                int len = inputStreamReader.read(chars);
                if (len == -1) {
                    break;
                }
                str = new String(chars, 0, len);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileInputStream.close();
                inputStreamReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return str;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        String cfgDoublePath = "F:\\nr-file\\5.cfg";
        String datDoublePath = "F:\\nr-file\\5.cfg";
        Map<String, String> mapNr = NrUtil.getInstace().ReadCFGAndGetParams(cfgDoublePath);
        // 获取Shex content
        String hexContent = NrUtil.getInstace().readDatFile(datDoublePath);
        List<Object> list = NrUtil.getInstace().datH2D(Integer.parseInt(mapNr.get("TT_channelNum")), hexContent,  Float.parseFloat(mapNr.get("a1")), Float.parseFloat(mapNr.get("b1")));
        System.out.println(list.get(0));
    }
}
