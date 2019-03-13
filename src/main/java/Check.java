public class Check {
//验证输入的数据是否为有效数字
    public String check(String s0, String s1, String s2){
        String error = "1";
        try{
            int r0 = Integer.parseInt(s0);
            int r1 = Integer.parseInt(s1);
            int r2 = Integer.parseInt(s2);
        }catch (NumberFormatException e){
            error = "请在R0,R1,R2中输入有效数字!!!";
        }
        return error;
    }
}
