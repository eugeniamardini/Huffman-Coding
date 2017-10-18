import java.io.FileInputStream;
import java.io.IOException;
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
 * The HuffmanTree class was made to build a binary tree with a particular structure from the contents 
 * of a Priority Queue (that has all unique characters and their count stored as nodes).
 * The purpose of such a tree is to create binary encodings of each character, and then
 * decode the encodings to get the original .txt format
 * 
 * Sources i used to build this class:
 * 1)https://github.com/sparkart/Huffman-Encoding---CS-Project/blob/master/HuffmanTreeELB.java
 * 2)http://stackoverflow.com/questions/21854069/decoding-huffman-tree
 */
public class HuffmanTree {
    public static PriorityQueue<HuffmanNode> pq = new PriorityQueue<HuffmanNode>();
    public StringBuilder s = new StringBuilder();
    private ArrayList<Character> ListOfChars;
    private HuffmanNode root;
    
    // constructor 
    public HuffmanTree (Map<Character, Integer> counts) {
	    FillSet(counts); 
	    for (Character c : counts.keySet()) {
	        pq.add(new HuffmanNode(counts.get(c),c, null,null)); //fill in the PriorityQueue with characters
	    }
        add(pq);
    }
    
    //This method fills all the characters in an ArrayList
	private void FillSet (Map<Character, Integer> counts) {
		ListOfChars=new ArrayList<Character>();
		for (char c:counts.keySet()) {
			ListOfChars.add(c);
		}
	}
	
	//This method creates encodings for all characters
	public Map<Character, String> encode() {
		Map<Character, String> map=new TreeMap<Character, String>();
		for(int i=0;i<ListOfChars.size();i++) {
			//creating a map with each character and its binary representation
			map.put(ListOfChars.get(i),encode(root,ListOfChars.get(i)));
		}
		return map;
	}
	
	//This recursive method creates a binary encoding for a given character
	private String encode (HuffmanNode node,char c) { 
		if (node.character==c && (node.left==null||node.right==null)) {
			return "";
		}
		else if (node.character!=c&&(node.left==null||node.right==null)) {
			return null;
		}
		String s1="0"+encode(node.left, c);
		String s2="1"+encode(node.right,c);
		if (s1.contains("null")) {
			return s2;
		}
		else if (s2.contains("null")) {
			return s1;
		}
		else {
			return null;
		}
	}
	
	//this method compresses gets a Map <Character, String> from encode()
	//and appends the string binary representation to the StringBuilder object that outputs
	//the binary code to the GUI
	public StringBuilder compress (FileInputStream input) throws IOException {
		if(input==null) {
			throw new IllegalArgumentException();
		}
		Map<Character, String> map=this.encode();
		int c=input.read();
		while(c!=-1) {
			s.append(map.get((char)c));
			c=input.read();
		} 
        return s;
	}
	
	public StringBuilder decompress (StringBuilder s) {
		return decompress(s.toString(),root);
	}
	
	// This helper method decompresses the compressed character into the original one
	private StringBuilder decompress(String s,HuffmanNode node) {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < s.length(); ++i) {
		    if (s.charAt(i) == '0') { // left child
			    node = node.left;
			} else { // rigth child
			    node = node.right;
			} 
		    if (node.isLeaf() == true) { //if a node doesn't have children, it must a character, so we append it
			    result.append(node.character);
			    node = root;//updating the node for the traversal
			}
		}
	    return result;
	}
	
	//printSideways() was written by an example in BJP textbook
	public String printSideways() {
        StringBuilder string = new StringBuilder("");
        printSideways(root, string , 0);
        return string.toString(); 
	}
	
    //this recursive method is a helper
	private void printSideways(HuffmanNode root, StringBuilder thing, int level) {
	    if (root != null) {
	        printSideways(root.right, thing, level+1);
	        thing.append("\n");
	        for(int i = 0; i < level; i++){
	            thing.append("    ");
	        }
	        thing.append(root.frequency+"="+root.character);
	        printSideways(root.left, thing , level+1);	
	    }
	}
	
	// This method creates the huffman tree
	private void add(PriorityQueue<HuffmanNode> q) { 
		while(q.size()>1) {
			//removing two front nodes and storing them into temporary nodes
		    HuffmanNode temp1=q.remove();
		    HuffmanNode temp2=q.remove();
		    //joining temp nodes (children) into a new one (parent)
		    HuffmanNode total=new HuffmanNode(temp1.frequency+temp2.frequency);
		    total.left=temp1;
			total.right=temp2;
			q.add(total);//new node is reinserted back into PriorityQueue
		}
		root=q.remove();//the last node left is the root
	}
}
