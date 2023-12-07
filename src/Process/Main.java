package Process;

import java.util.Scanner;
import java.util.LinkedList;
import java.util.Queue;
import Memory.*;

public class Main {
	static int mulcount;
	static int addcount;
	static int loadcount;
	static int storecount;
	static int mulspace;
	static int addspace;
	static int loadspace;
	static int storespace;
	int clock;
	int line;
	int lengthOfArrays;
	Memory mainMemory;
	Queue<String> fetch = new LinkedList<String>();
	Queue<String> issue = new LinkedList<String>();
	Queue<String> execute = new LinkedList<String>();
	Queue<String> write = new LinkedList<String>();
	int[] insts;
	int[] result;
	ReservationStations reservationStations;
	RegisterFile registerFile;
	InstructionsTable instructionsTable;
	boolean stopFetching;
	boolean startFetching;
	
	public Main() {
		clock = 0;
		line = 0;
		mainMemory = new Memory("Instructions");
		if(mainMemory.isIteration())
			lengthOfArrays = 1000;
		else
			lengthOfArrays = mainMemory.getOperations().length;
		instructionsTable = new InstructionsTable(lengthOfArrays);
		insts = new int[lengthOfArrays];
		result = new int[lengthOfArrays];
		//reservationStations = new ReservationStations(mulspace,addspace,loadspace,storespace,mulcount,addcount,loadcount,storecount);
		reservationStations = new ReservationStations();
		registerFile = new RegisterFile();
		loadInstructions(0);
		stopFetching = false;
		startFetching = false;
	}
	
	public void run() {
		while(!fetch.isEmpty() || !issue.isEmpty() || !execute.isEmpty() || !write.isEmpty() || clock == 0 || startFetching) {
			if(clock==1000) {
				System.out.println("Timed out");
				break;
			}
			System.out.println("********************** Clock Cycle: " + clock + " **********************");
			if(line < mainMemory.getOperations().length) {
				if(mainMemory.getLabel(line) == null && !stopFetching) {
					startFetching = false;
					fetch.add((String)mainMemory.getOperationsWithLocation(line) + " " + line);
					instructionsTable.setIteration(clock);
					if(((String)mainMemory.getOperationsWithLocation(line)).startsWith("BNEZ")) {
						stopFetching = true;
					}
				} else if(mainMemory.getLabel(line) != null) {
					startFetching = true;
					instructionsTable.incrementIteration();
					loadInstructions(mainMemory.getOperations().length*instructionsTable.getIter());
				}
				line++;
			}
			printQueues();
			if(!write.isEmpty() && clock>=3) {
				writeMethod();
			}
			if(!execute.isEmpty() && clock>=2) {
				executeMethod();
			}
			if(!issue.isEmpty() && clock>=1) {
				issueMethod();
			}
			if(!fetch.isEmpty()) {
				isOccupied();
				for(String value: issue) {
					if(fetch.contains(value))
						fetch.remove(value);
				}
			}
			print();
			clock++;
		}
		System.out.println("************************************************************");
	}
	
	public void loadInstructions(int index) {
		int counting = index;
		instructionsTable.setInstructions(mainMemory.getOperations());
		for(int i=0;i<mainMemory.getOperations().length;i++) {
			counting = index + i;
			if(mainMemory.getOperationsWithLocation(i).startsWith("MUL") || mainMemory.getOperationsWithLocation(i).startsWith("DIV")){
				instructionsTable.setDestinationRegister(counting, "F"+mainMemory.getDestination(i));
				instructionsTable.setJ(counting, "F"+mainMemory.getRegister2(i));
				instructionsTable.setK(counting, "F"+mainMemory.getRegister3(i));
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("ADD") || mainMemory.getOperationsWithLocation(i).startsWith("SUB")) {
				if(mainMemory.getOperationsWithLocation(i).startsWith("ADDI") || mainMemory.getOperationsWithLocation(i).startsWith("SUBI")) {
					instructionsTable.setDestinationRegister(counting, "R"+mainMemory.getDestination(i));
					instructionsTable.setJ(counting, "R"+mainMemory.getRegister2(i));
					instructionsTable.setK(counting, ""+mainMemory.getImm(i));
				} else {
					instructionsTable.setDestinationRegister(counting, "F"+mainMemory.getDestination(i));
					instructionsTable.setJ(counting, "F"+mainMemory.getRegister2(i));
					instructionsTable.setK(counting, "F"+mainMemory.getRegister3(i));
				}
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("L")) {
				instructionsTable.setDestinationRegister(counting, "F"+mainMemory.getDestination(i));
				instructionsTable.setJ(counting, ""+mainMemory.getAddressposition(i));
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("S")) {
				instructionsTable.setDestinationRegister(counting, "F"+mainMemory.getDestination(i));
				instructionsTable.setJ(counting, ""+mainMemory.getAddressposition(i));
			} else if(mainMemory.getOperationsWithLocation(i).startsWith("BNEZ")) {
				instructionsTable.setDestinationRegister(counting, "R"+mainMemory.getJump(i));
				instructionsTable.setJ(counting, mainMemory.getBranch(i));
			}
			insts[counting] = -1;
		}
	}
	
	public void isOccupied() {
		String find = fetch.peek();
		if(find.startsWith("MUL") || find.startsWith("DIV")) {
			if(!reservationStations.isOccupiedMul())
				issue.add(find);
		} else if(find.startsWith("ADD") || find.startsWith("SUB") || find.startsWith("BNEZ")) {
			if(!reservationStations.isOccupiedAdd())
				issue.add(find);
		} else if(find.startsWith("L")) {
			if(!reservationStations.isOccupiedLoad())
				issue.add(find);
		} else if(find.startsWith("S")) {
			if(!reservationStations.isOccupiedStore())
				issue.add(find);
		}
	}
	
	public boolean isWaiting(String find) {
		int index = Integer.parseInt(find.split(" ")[1]);
		if(find.startsWith("MUL") || find.startsWith("DIV")){
			if(reservationStations.getQjmul(index).equals("0") && reservationStations.getQkmul(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("ADD") || find.startsWith("SUB")) {
			if(reservationStations.getQjadd(index).equals("0") && reservationStations.getQkadd(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("S")) {
			if(reservationStations.getQstore(index).equals("0")) {
				return false;
			} else
				return true;
		} else if(find.startsWith("L")) {
			
		}
		return false;
	}
	
	public void issueMethod() {
		String operation = (String)issue.peek().split(" ")[0];
		int issued = Integer.parseInt((String)issue.peek().split(" ")[1]);
		String destinationRegister = instructionsTable.getDestinationRegister(issued);
		if(operation.startsWith("MUL") || operation.startsWith("DIV") || operation.startsWith("ADD") || operation.startsWith("SUB")){
			String value1 = registerFile.getQ(destinationRegister);
			String value2 = registerFile.getQ(instructionsTable.getJ(issued));
			String value3 = registerFile.getQ(instructionsTable.getK(issued));
			String reg2 = "";
			String reg3 = "";
			if(value2.equals("0")) {
				reg2 = instructionsTable.getJ(issued);
			}
			if(value3.equals("0")) {
				reg3 = instructionsTable.getK(issued);
			}
			if(operation.startsWith("MUL") || operation.startsWith("DIV")) {
				reservationStations.setOccupiedMul(operation,reg2,reg3,value2,value3,-1,issued);
				insts[issued] = reservationStations.getMaxmul();
			} else {
				reservationStations.setOccupiedAdd(operation,reg2,reg3,value2,value3,-1,issued);
				if(operation.startsWith("ADDI"))
					insts[issued] = 1;
				else {
					insts[issued] = reservationStations.getMaxadd();
				}
			}
			if(value1.equals("0")) {
				registerFile.setQ(destinationRegister, reservationStations.getTagUsingLine(issued));
			}
		} else if(operation.startsWith("L")) {
			String value = registerFile.getQ(destinationRegister);
			reservationStations.setOccupiedLoad(mainMemory.getAddressposition(issued),issued);
			insts[issued] = reservationStations.getMaxload();
			if(value.equals("0")) {
				registerFile.setQ(destinationRegister, reservationStations.getTagUsingLine(issued));
			}
		} else if(operation.startsWith("S")) {
			String value = registerFile.getQ(destinationRegister);
			String reg = "";
			if(value.equals("0")) {
				reg = destinationRegister;
			}
			reservationStations.setOccupiedStore(mainMemory.getAddressposition(issued),reg,value,issued);
			insts[issued] = reservationStations.getMaxstore();
		} else if(operation.startsWith("BNEZ")){
			String value1 = registerFile.getQ(destinationRegister);
			String reg2 = instructionsTable.getJ(issued);
			reservationStations.setOccupiedAdd(operation,reg2,"","0","0",-1,issued);
			insts[issued] = 1;
			if(value1.equals("0")) {
				registerFile.setQ(destinationRegister, reservationStations.getTagUsingLine(issued));
			}
		}
		instructionsTable.setIssue(issued, clock);
		execute.add(issue.remove());
	}
	
	public void executeMethod() {
		for(String value: execute) {
			if(!isWaiting(value)) {
				int executed = Integer.parseInt(value.split(" ")[1]);
				String operation = value.split(" ")[0];
				if(operation.startsWith("MUL") || operation.startsWith("DIV")){
					if(insts[executed]==reservationStations.getMaxmul())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("ADD") || operation.startsWith("SUB")) {
					if(insts[executed]==reservationStations.getMaxadd())
						instructionsTable.setExecutionStart(executed, clock);
					else if(operation.startsWith("ADDI") && insts[executed]==1)
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("L")) {
					if(insts[executed]==reservationStations.getMaxload())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("S")) {
					if(insts[executed]==reservationStations.getMaxstore())
						instructionsTable.setExecutionStart(executed, clock);
				} else if(operation.startsWith("BNEZ")) {
					if(insts[executed]==1)
						instructionsTable.setExecutionStart(executed, clock);
				}
				if(insts[executed]==0) {
					if(operation.startsWith("MUL")) {
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) * registerFile.getContent(instructionsTable.getK(executed));
					} else if(operation.startsWith("DIV")){
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) / registerFile.getContent(instructionsTable.getK(executed));
					} else if(operation.startsWith("ADDI")) {
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) + mainMemory.getImm(executed);
					} else if(operation.startsWith("SUBI")) {
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) - mainMemory.getImm(executed);
					} else if(operation.startsWith("ADD")) {
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) + registerFile.getContent(instructionsTable.getK(executed));
					} else if(operation.startsWith("SUB")) {
						result[executed] = registerFile.getContent(instructionsTable.getJ(executed)) - registerFile.getContent(instructionsTable.getK(executed));
					} else if(operation.startsWith("L")) {
						result[executed] = (int)mainMemory.getMemoryWithLocation(reservationStations.getAddressload(executed));
					} else if(operation.startsWith("S")) {
						result[executed] = registerFile.getContent(reservationStations.getVstore(executed));
					}
					if(operation.startsWith("BNEZ")) {
						if(registerFile.getQ(instructionsTable.getDestinationRegister(executed)).equals("0")) {
							if(registerFile.getContent(instructionsTable.getDestinationRegister(executed)) != 0)
								line = mainMemory.getLabelWithString(mainMemory.getBranch(executed));
							stopFetching = false;
							instructionsTable.setExecutionComplete(executed, clock);
							insts[executed]=-2;
						}
					} else {
						instructionsTable.setExecutionComplete(executed, clock);
						insts[executed]=-2;
					}
				} else {
					if(insts[executed]!=0) {
						insts[executed]--;
					}
				}
			}
		}
		for(int i=0;i<insts.length;i++) {
			if(insts[i]==-2) {
				execute.remove(mainMemory.getOperationsWithLocation(i) + " " + i);
				write.add(mainMemory.getOperationsWithLocation(i) + " " + i);
				insts[i]=-1;
			}
		}
	}
	
	public void writeMethod() {
		int written = Integer.parseInt((String)write.peek().split(" ")[1]);
		String operation = (String)write.peek().split(" ")[0];
		String registerWrite = instructionsTable.getDestinationRegister(written);
		
		if(operation.startsWith("MUL") || operation.startsWith("DIV") || operation.startsWith("ADD") || operation.startsWith("SUB") || operation.startsWith("L")){
			if(registerFile.getQ(registerWrite).equals(reservationStations.getTagUsingLine(written)) || registerFile.getQ(registerWrite).equals("0")) {
				reservationStations.writeWaiting(registerFile.getQ(registerWrite), registerWrite);
				registerFile.setContent(registerWrite, result[written]);
			}
		} else if(operation.startsWith("S")) {
			mainMemory.setMemory(reservationStations.getAddressstore(written),result[written]);
		}
		reservationStations.setAvailable(written);
		instructionsTable.setWriteResult(written, clock);
		write.remove();
	}
	
	public void printQueues() {
		if(!fetch.isEmpty()) {
			System.out.print("Fetch Queue: ");
			for(String value: fetch) {
				System.out.print(value.split(" ")[1] + "  ");
			}
			System.out.println();
		}
		if(!issue.isEmpty()) {
			System.out.print("Issue Queue: ");
			for(String value: issue) {
				System.out.print(value.split(" ")[1] + "  ");
			}
			System.out.println();
		}
		if(!execute.isEmpty()) {
			System.out.print("Execute Queue: ");
			for(String value: execute) {
				System.out.print(value.split(" ")[1] + "  ");
			}
			System.out.println();
		}
		if(!write.isEmpty()) {
			System.out.print("Write Queue: ");
			for(String value: write) {
				System.out.print(value.split(" ")[1] + "  ");
			}
			System.out.println();
		}
	}
	
	public void print() {
		mainMemory.toString();
		reservationStations.toString();
		registerFile.toString();
		instructionsTable.toString();
	}
	
	public static void main(String[] args) {
//		Scanner obj = new Scanner(System.in);
//		System.out.println("Enter size of mul reservation station");
//		mulspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of add reservation station");
//		addspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of load reservation station");
//		loadspace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of store reservation station");
//		storespace = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of mul cycles");
//		mulcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of add cycles");
//		addcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of load cycles");
//		loadcount = Integer.parseInt(obj.nextLine());
//		System.out.println("Enter size of store cycles");
//		storecount= Integer.parseInt(obj.nextLine());
//		System.out.println(mulspace + " " + addspace + " " + loadspace + " " + storespace);
//		System.out.println(mulcount + " " + addcount + " " + loadcount + " " + storecount);
//		obj.close();
		Main main = new Main();
		main.run();
	}
}
