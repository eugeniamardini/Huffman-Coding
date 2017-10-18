import java.io.*;
import java.util.*;
import java.util.Map.Entry;
/*
 * Student: Yauheniya Zapryvaryna
 * Instructor: William Iverson
 * Bellevue College
 * CS 211
 * June 16, 2014
 * Assignment "Huffman Coding"
 * 
 * The HuffmanNode class was made to construct a node to be placed into HuffmanTree 
 * and for further compressing and decompressing
 */

public class HuffmanNode implements Comparable <HuffmanNode>{
	public static final char EOF=3;
	public int frequency;
	public char character;
	public HuffmanNode left;
	public HuffmanNode right;
	
	//default constructor
    public HuffmanNode(){
    }
    
    //constructor 
    public HuffmanNode (int frequency, char character, HuffmanNode left, HuffmanNode right) {
    	this.character = character;
    	this.frequency = frequency;
    	this.left = left;
    	this.right = right;
    }
    
    // Constructor taking just a frequency
	public HuffmanNode (int frequency) {
		this.frequency=frequency;
		this.left=null;
		this.right=null;
	}
   
	//provides count of characters in an input file, places them into a map 
	public static Map<Character, Integer> getCounts (FileInputStream input) throws IOException {
		Map<Character, Integer> map = new TreeMap<>();
		try {
			while (input.read() != -1) {
				//taken from my Vocabulary3.java (previous assignment)
				char current;
			    while (input.available() > 0) {
			        current = (char)input.read();
			        if(map.containsKey(current)){
		    		    map.put(current, map.get(current)+1); // if contains this char, add to count 
		    		}
		    		else
		    			map.put(current, 1); //if not, add to the map
			    }
			    map.put(EOF, 1);
			}
        } catch (IOException e) {
			throw new IOException("Error reading file");
		}
    	return map;
	}

	//returns whether the node has any children
    public boolean isLeaf(){
    	if ((left != null) && (right != null)) {
    		return false;
    	} else {
    		return true;
    	}
    }
    
    //method used by a PriorityQueue sorting
	public int compareTo(HuffmanNode other) {
		return this.frequency - other.frequency;		
	}
}
