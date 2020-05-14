public class Random {
    public static void main(String[] args) {
        String p = "~/mnt/production/abcd";
        while (p.endsWith("/")) {
            p = p.substring(0, p.length() - 1);
        }
        System.out.println(p + " done here");
    }
}
