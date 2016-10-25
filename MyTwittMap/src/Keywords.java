
public class Keywords {
    public String keyword(String text) {
        String result = "";
        if (text.toLowerCase().contains("food")) {
            result = "food";
        } else if (text.toLowerCase().contains("love")) {
            result = "love";
        } else if (text.toLowerCase().contains("job")) {
            result = "job";
        } else if (text.toLowerCase().contains("fashion")) {
            result = "fashion";
        } else if (text.toLowerCase().contains("trump")) {
            result = "trump";
        } else if (text.toLowerCase().contains("hillary")) {
            result = "hillary";
        } else if (text.toLowerCase().contains("vegas")) {
            result = "vegas";
        } else if (text.toLowerCase().contains("lol")) {
            result = "lol";
        } else if (text.toLowerCase().contains("newyork")|| text.toLowerCase().contains("new york")|| text.toLowerCase().contains("ny")) {
            result = "newyork";
        }
        else {
            result = null;
        }
        return result;
    }

}