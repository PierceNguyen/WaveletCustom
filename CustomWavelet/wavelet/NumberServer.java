import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

class Handler implements URLHandler {
    // The one bit of state on the server: a number that will be manipulated by
    // various requests.
    int num = 0;
    ArrayList<String> words = new ArrayList<String>();


    public String handleRequest(URI url) {
        if (url.getPath().equals("/")) {
            return String.format("Number: %d", num);
        } else if (url.getPath().equals("/increment")) {
            num += 1;
            return String.format("Number incremented!");
        } else {
            if (url.getPath().contains("/add")) {
                String[] parameters = url.getQuery().split("=");
                //return String.format(parameters[0] + parameters[1]);
                if (parameters[0].equals("count")) {
                    num += Integer.parseInt(parameters[1]);
                    return String.format("Number increased by %s! It's now %d", parameters[1], num);
                }
            }
            else if(url.getPath().contains("/new")){
                String parameters = url.getQuery();
                //return String.format(parameters[0] + parameters[1]);
                if(!words.contains(parameters)){
                    words.add(parameters);
                    String returnStr = String.format("Words added! \n" + "Words are now: \n");
                    for(int i = 0; i < words.size();i++){
                        returnStr = returnStr + words.get(i) + " ";
                    }
                    returnStr = returnStr + "\n";
                    return String.format(returnStr);
                }
                return String.format("This word has already been added");
            }
            else if(url.getPath().contains("/search")){
                String parameters = url.getQuery();
                boolean test = false;
                //return String.format(parameters[0] + parameters[1]);
                String returnStr = String.format("Matches found! \n" + "Matching words are: \n");
                for(int i = 0; i < words.size();i++){
                    if(words.get(i).contains(parameters)){
                        test = true;
                        returnStr += String.format(words.get(i) + " ");
                    }
                }
                returnStr = returnStr + "\n";
                if(test){
                    return String.format(returnStr);
                }                
                return String.format("No word matches!");
            }
            else if(url.getPath().contains("/clear")){
                words.clear();
                return String.format("Word list has been cleared");
            }
            return "404 Not Found!";
        }
    }
}

class NumberServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
