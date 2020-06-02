package feverengine;


/**
 * The parser class is designed to standardise the user's input strings in order to reduce the
 * amount of analysis that must be done in the controller. This involves making the string 
 * lower case, removing punctuation and removing synonyms in the the context of the 
 * game mechanics.
 * 
 * Faegan White
 * v0.1
 */
public class Parser
{
    public String parse(String input){
        // encase input in spaces to ease the destinguishing of words
        input = " " + input + " ";
        
        // Make String lower case
        input = input.toLowerCase();
        
        // Remove all punctuation
        String[] punctuation = {"!","?",".",",","'",";",":","'","|","/","(",")","]","[","#"};
        for (String remove: punctuation){
            input = input.replace(remove,"");
        }
        
        // Remove unecessary words - spaces ensure it's not part of another word
        String[] wordsRemove = {" the ", " a "," in "};
        for (String remove: wordsRemove){
            input = input.replace(remove," ");
        }
        
        /* keyWords is a 2d array storing the standardised terms in the first row with the 
        synonmys to be replaced in the sequencial rows */
        String[][] keyWords = {{"north","east","south","west","go","look","take","wait","quit","drop","y","n"}
                                ,{"n","e","s","w","move","examine","carry","stand","end","put","yes","no"}
                                ,{"up","right","down","left","walk","study","pick north","pause","stop","","",""}
                                ,{"","","","","run","look at","pick","dont move","","","",""}
                                ,{"","","","","","","get","","","","",""}};
        /* Check for any occurrence of a synonmy and replace it with the standard found in
        the first row. */
        for (int a = 0; a < keyWords[0].length; a+=1){
            for (int b = 1; b < keyWords.length; b+=1){
                input = input.replace(" " + keyWords[b][a] + " ", " " + keyWords[0][a] + " ");
            }
        }
        
        // Remove the spaces on the ends
        input = input.substring(1,input.length()-1);
        return input;
    }
}
