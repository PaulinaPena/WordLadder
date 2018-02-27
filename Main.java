/* WORD LADDER Main.java
 * EE422C Project 3 submission by
 * Paulina Vazquez-Pena
 * pv5329
 * 15510
 * Slip days used: <0>
 * Git URL: 
 * Spring 2018
 */


package assignment3;
import java.util.*;
import java.io.*;

public class Main {
	
	// static variables and constants only here.
	
	public static void main(String[] args) throws Exception {
		
		Scanner kb;	// input Scanner for commands
		PrintStream ps;	// output file, for student testing and grading only
		// If arguments are specified, read/write from/to files instead of Std IO.
		if (args.length != 0) {
			kb = new Scanner(new File(args[0]));
			ps = new PrintStream(new File(args[1]));
			System.setOut(ps);			// redirect output to ps
		} else {
			kb = new Scanner(System.in);// default input from Stdin
			ps = System.out;			// default output to Stdout
		}
		initialize();
		
		// TODO methods to read in words, output ladder
		
		ArrayList<String> input = parse(kb);
		if(input != null) {
			/**Breadth First Test*/
			ArrayList<String> breadth = getWordLadderBFS(input.get(0), input.get(1));
			System.out.println("Breadth First Search: " + '\n');
			printLadder(breadth);
			System.out.println('\n');
			/**Depth First Test*/
			System.out.println("Depth First Search: " + '\n');
			ArrayList<String> depth = getWordLadderDFS(input.get(0), input.get(1));
			printLadder(depth);
		}
		else {
			return;
		}

	}
	
	public static void initialize() {
		// initialize your static variables or constants here.
		// We will call this method before running our JUNIT tests.  So call it 
		// only once at the start of main.
	}
	
	/**
	 * The parse function obtains the user input and creates an
	 * ArrayList of the entries, if any.
	 * 
	 * @param keyboard Scanner connected to System.in
	 * @return ArrayList of Strings containing start word and end word. 
	 * If command is /quit, return empty ArrayList. 
	 */
	public static ArrayList<String> parse(Scanner keyboard) {
		// TODO
		/**Obtaining input words*/
		ArrayList<String> parse = new ArrayList<String>();
		
		System.out.print("Enter a word pair: ");
		String start = keyboard.next();
		
		/**Quit Condition*/
		if(start.equals("quit")) {
			return null;
		}
		
		String end = keyboard.next();
		System.out.println('\n');
		
		keyboard.close();
		
		/**Quit Condition*/
		if(start.equals("quit") || end.equals("quit")) {
			return null;
		}
		
		/**Same length Condition*/
		else if(start.length() != end.length()) {
			return null;
		}
		/**Adding words to the array list of inputs*/
		else {
			parse.add(start);
			parse.add(end);	
		}
		return parse;
	}
	
	/**
	 * The getWordLadderDFS function obtains the Word Ladder
	 * between the start and end words. 
	 * 
	 * @param String start, String end
	 * @return ArrayList of Strings containing the word ladder by the DFS method. 
	 * If there is no word ladder, it will return an ArrayList with the start and end words.
	 */
	public static ArrayList<String> getWordLadderDFS(String start, String end) {
		// TODO some code
		/**Dictionary and visited ArrayList declared*/
		Set<String> dict = makeDictionary();
		Set<String> visited = new HashSet<String>();
		ArrayList<String> result = new ArrayList<String>();
		/**ArrayList to be returned is result*/
		
		try{
			result = helperDFS(start.toUpperCase(), end.toUpperCase(), dict, visited);
		}
		catch(StackOverflowError e) {
			result = null;
		}
		
		
		/**If there is no word ladder, return the start and end word in the ArrayList*/
		if(result == null) {
			result = new ArrayList<String>();
	 		result.add(start.toLowerCase());
			result.add(end.toLowerCase());
		}
		return result;
		// TODO more code
	}
	
	/**
	 * The helperDFS uses recursion to find a word ladder between the
	 * start and end words, if any.
	 * 
	 * @param String current, String end, Set<String> dict, Set<String> visited.
	 * @return ArrayList of Strings containing the word ladder by the DFS method. 
	 * If there is no word ladder, it will return a null ArrayList.
	 */
	public static ArrayList<String> helperDFS(String current, String end, Set<String> dict,  Set<String> visited){
		
		/**Base Case, creates the ArrayList to return if the word is found*/
		if(current.equals(end)) {
			ArrayList<String> result = new ArrayList<String>();
			result.add(current);
			return result;
		}
		
		/**Adds current to visited, creates the first generation of words after current*/
		visited.add(current);
		ArrayList<String> generation = oneLetterDiff(dict,current,end);
		
		/**Iterates through the generations to find the path*/
		for(String g : generation) {
			if(!visited.contains(g)) {
				ArrayList<String> x = helperDFS(g,end,dict,visited);
				if(x != null) {
					x.add(current);
					return x;
				}
			}
		}
		/**Returns null if no path is found*/
		return null;
	}
	
	/**
	 * The getWordLadderBFS function returns the word ladder
	 * found between the start and end words, if there is one.
	 * 
	 * @param String start, String end 
	 * @return ArrayList of Strings containing word ladder, if found. 
	 * returns an ArrayList with only the start and end words, if any.  
	 */
    public static ArrayList<String> getWordLadderBFS(String start, String end) {
		// TODO some code
    	/**Initializations*/
		Set<String> dict = makeDictionary();
		Set<String> visited = new HashSet<String>();
		ArrayList<String> generation = new ArrayList<String>();
		ArrayList<String> path = new ArrayList<String>(); 
		TreeMap<String,String> map = new TreeMap<String, String>();
		Queue<String> q = new LinkedList<>();
		String current = start.toUpperCase();
		
		/**Add start word to queue, initial key to map*/
		q.add(start.toUpperCase());
		map.put(current,null);
		
		/**While the queue is not empty, find the generations of each current and check if any
		 * child is the end word by using FIFO of queue*/
		while(!q.isEmpty()) {
			current = q.poll();
			generation = oneLetterDiff(dict,current,end);
			
			visited.add(current);
			
			for(String n : generation) {
				if(!visited.contains(n)) {
					q.add(n);
					map.put(n,current);
					visited.add(n);
				}
			}
			
			if(current.equals(end.toUpperCase())) {
				String copy = current;
				while(copy != null) {
					path.add(copy);
					copy = map.get(copy);
				}
				return path;
			}	
		}
		/**Else, add the start and end words to the ArrayList to return*/
		path.add(start.toLowerCase());
		path.add(end.toLowerCase());
		return path; 
	}
    
    /**
	 * The printLadder function reverses the ladder and prints
	 * if there is one.
	 * 
	 * @param ArrayList<String> ladder  
	 *   
	 */
	public static void printLadder(ArrayList<String> ladder) {
		/**Reverses the ladder*/
		ArrayList<String> reverse = new ArrayList<String>();
		
		/**For a ladder that only has the start and end words only*/
		if(ladder.size() == 2) {
			
			char [] first = ladder.get(0).toCharArray();
			char [] second = ladder.get(1).toCharArray();
			int count = 0;
			
			/**If the words have more than one difference, they are not word ladders*/
			for(int i = 0; i < first.length; i++){
				if(first[i] != second[i]) {
					count++;
				}
			}
			if(count>1) {
				System.out.println("no word ladder can be found between " + ladder.get(0) + " and " + ladder.get(1));
				return;
			}
			
			/**Printing message*/
			int rung = ladder.size() - 2;
			System.out.println("a " + rung + "-rung word ladder exists between " + ladder.get(ladder.size()-1).toLowerCase() + " and " + ladder.get(0).toLowerCase());
			System.out.println(ladder.get(1).toLowerCase());
			System.out.println(ladder.get(0).toLowerCase());
			return;
		}
		
		/**Reverses word ladder*/
		for(int i = ladder.size() - 1; i >= 0; i--) {
			reverse.add(ladder.get(i));
			
		} 
		
		/**Printing message*/
		int rung = ladder.size() - 2;
		System.out.println("a " + rung + "-rung word ladder exists between " + ladder.get(ladder.size()-1).toLowerCase() + " and " + ladder.get(0).toLowerCase());
		for (String r : reverse) {
			System.out.println(r.toLowerCase());
		}
	}
	
	/**
	 * The oneLetterDiff function finds all the words with a one letter difference 
	 * from the start word, placing preference for those that are most similar to
	 * the end word.
	 * 
	 * @param Set<String> dict, String start, String end 
	 * @return ArrayList of Strings containing a generation of one letter difference. 
	 *   
	 */
	public static ArrayList<String> oneLetterDiff(Set<String> dict, String start, String end){
		
		/**Initializations*/
		/**Sets are used to prevent duplicates*/
		Set<String> firstLetters = new HashSet<String>();
		Set<String> secondLetters = new HashSet<String>();
		ArrayList<String> fin = new ArrayList<String>();
		
		String copy = start.toUpperCase();
		String ecopy = end.toUpperCase();
		
		char [] endWord = ecopy.toCharArray();
		
		for(int i = 0; i < start.length() ; i++) {
			/**Places the words most similar to end word and one letter difference from the start word at 
			 * the beginning of the ArrayList*/
			char[] word = copy.toCharArray(); 
			char [] temp = copy.toCharArray();
			temp[i] = endWord[i];
			String replace = new String(temp);
			if(dict.contains(replace) && !replace.equals(copy) && !secondLetters.contains(replace)){
				firstLetters.add(replace);
			}
			
			/**Finds all other one letter difference words*/
			for(char x = 'A'; x < 'Z' + 1; x++) {
				word[i] = x;
				String cry = new String(word);
				if(dict.contains(cry) && !cry.equals(copy) && !secondLetters.contains(cry) && !firstLetters.contains(cry)) {
					secondLetters.add(cry);
				}
				
			}
		
		}	
		
		fin.addAll(firstLetters);
		fin.addAll(secondLetters);
		
		return fin; 
	}
	

	/* Do not modify makeDictionary */
	public static Set<String>  makeDictionary () {
		Set<String> words = new HashSet<String>();
		Scanner infile = null;
		try {
			infile = new Scanner (new File("five_letter_words.txt"));
		} catch (FileNotFoundException e) {
			System.out.println("Dictionary File not Found!");
			e.printStackTrace();
			System.exit(1);
		}
		while (infile.hasNext()) {
			words.add(infile.next().toUpperCase());
		}
		return words;
	}
}
