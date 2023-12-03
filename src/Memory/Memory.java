package Memory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Memory {
	Object[] Memory = new Object[500];
	Object[] read = new Object[100];
	String[] operations;
	int[] destination;
	int[] addressposition;
	int[] register1;
	int[] register2;
	int[] register3;
	int[] imm;
	int[] jump;
	String[] branch;
	public static int count = 0;

	public Memory(String fileName) {
		try {
			FileReader file = new FileReader(fileName);
			BufferedReader myReader = new BufferedReader(file);
			String line;

			while ((line = myReader.readLine()) != null) {
				read[count] = line;
				count++;
			}
			setOperations();
			file.close();
			myReader.close();

		} catch (IOException e) {
			System.out.println("An error occurred.");
		}
	}

	public Object[] getMemory() {
		return Memory;
	}
	
	public Object getMemoryWithLocation(int i) {
		return Memory[i];
	}

	public void setMemory(int x, Object y) {
		Memory[x] = y;
	}
	
	public Object[] getRead() {
		return read;
	}
	
	public Object getReadWithLocation(int i) {
		return read[i];
	}

	public void setRead(int x, Object y) {
		read[x] = y;
	}
	
	public String[] getOperations() {
		return operations;
	}
	
	public String getOperationsWithLocation(int i) {
		return operations[i];
	}
	
	public void setOperations() {
		int c = 0;
		for(int i=0;i<read.length;i++) {
			if(read[i]!=null)
				c++;
		}
		operations = new String[c];
		destination = new int[c];
		addressposition = new int[c];
		register1 = new int[c];
		register2 = new int[c];
		register3 = new int[c];
		imm = new int[c];
		jump = new int[c];
		branch = new String[c];
		for(int i=0;i<c;i++) {
			destination[i] = -1;
			register1[i] = -1;
			register2[i] = -1;
			register3[i] = -1;
			imm[i] = -1;
			jump[i] = -1;
		}
		for(int i=0;i<c;i++) {
			operations[i] = (((String)read[i]).split(" "))[0];
			if(operations[i].equals("L.D") || operations[i].equals("S.D")) {
				String result1;
				String result2;
				result1 = (((String)read[i]).split(" "))[1];
				result2 = (((String)read[i]).split(","))[1];
				//System.out.println("load/store: " + result1 + " " + result2);
				for(int j = 0;j<32;j++) {
					if(result1.contains("F"+j)) {
						destination[i] = j;
					}
				}
				addressposition[i] = Integer.parseInt(result2);
			} else if(operations[i].equals("ADD.D") || operations[i].equals("SUB.D") || operations[i].equals("MUL.D") || operations[i].equals("DIV.D")){
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				//System.out.println("floating point: " + result1 + " " + result2 + " " + result3);
				for(int j = 0;j<32;j++) {
					if(result1.equals("F"+j))
						register1[i] = j;
					if(result2.equals("F"+j))
						register2[i] = j;
					if(result3.equals("F"+j))
						register3[i] = j;
				}
			} else if(operations[i].equals("ADDI") || operations[i].equals("SUBI")) {
				String result1;
				String result2;
				String result3;
				result1 = ((((String)read[i]).split(" "))[1]).split(",")[0];
				result2 = (((String)read[i]).split(","))[1];
				result3 = (((String)read[i]).split(","))[2];
				//System.out.println("integer: " + result1 + " " + result2 + " " + result3);
				for(int j = 0;j<32;j++) {
					if(result1.equals("R"+j))
						register1[i] = j;
					if(result2.equals("R"+j))
						register2[i] = j;
				}
				imm[i]=Integer.parseInt(result3);
			} else if(operations[i].equals("BNEZ")) {
				String result;
				result = (((String)read[i]).split(" "))[1];
				//System.out.println("jump: " + result + " " + (((String)read[i]).split(","))[1]);
				for(int j = 0;j<32;j++) {
					if(result.equals("R"+j))
						jump[i] = j;
				}
				branch[i] = (((String)read[i]).split(","))[1];
			}
		}
	}
	
	public int getDestination(int n) {
		return destination[n];
	}
	public int getAddressposition(int n) {
		return addressposition[n];
	}
	public int getRegister1(int n) {
		return register1[n];
	}
	public int getRegister2(int n) {
		return register2[n];
	}
	public int getRegister3(int n) {
		return register3[n];
	}
	public int getImm(int n) {
		return imm[n];
	}
	public int getJump(int n) {
		return jump[n];
	}
	public String getBranch(int n) {
		return branch[n];
	}
	
	public String toString() {
		System.out.println("------------------------Memory------------------------");
		for(int i=0;i<Memory.length;i++) {
			if(Memory[i]!=null)
				System.out.println(i + ": " + Memory[i]);
		}
//		System.out.println("---------------------Instructions---------------------");
//		for(int i=0;i<read.length;i++) {
//			if(read[i]!=null)
//				System.out.println(read[i]);
//		}
//		System.out.println("---------------------Operations---------------------");
//		for(int i=0;i<operations.length;i++) {
//			System.out.println(operations[i]);
//		}
//		System.out.println("---------------------Destination---------------------");
//		for(int i=0;i<destination.length;i++) {
//			System.out.println(destination[i]);
//		}
//		System.out.println("------------------Effective Address------------------");
//		for(int i=0;i<addressposition.length;i++) {
//			System.out.println(addressposition[i]);
//		}
//		System.out.println("---------------------Register 1---------------------");
//		for(int i=0;i<register1.length;i++) {
//			System.out.println(register1[i]);
//		}
//		System.out.println("---------------------Register 2---------------------");
//		for(int i=0;i<register2.length;i++) {
//			System.out.println(register2[i]);
//		}
//		System.out.println("---------------------Register 3---------------------");
//		for(int i=0;i<register3.length;i++) {
//			System.out.println(register3[i]);
//		}
//		System.out.println("---------------------Immediate---------------------");
//		for(int i=0;i<imm.length;i++) {
//			System.out.println(imm[i]);
//		}
//		System.out.println("------------------------Jump------------------------");
//		for(int i=0;i<jump.length;i++) {
//			System.out.println(jump[i]);
//		}
//		System.out.println("-----------------------Branch-----------------------");
//		for(int i=0;i<branch.length;i++) {
//			System.out.println(branch[i]);
//		}
		return "";
	}
}