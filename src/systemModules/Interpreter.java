package systemModules;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import submodules.PCB;
import system.OS;
import system.SystemCalls;

public class Interpreter {
	public Process readProgram(int programID) {
		String filePath = "programFiles/Program_" + programID + ".txt";
		Process process = new Process();
		process.setPCB(new PCB());
		process.getPCB().setPID(programID);
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String line = br.readLine();
			while (line != null) {
				if (line.startsWith("assign")) {
					String[] assignInstr = line.split(" ");
					if (assignInstr[2].equalsIgnoreCase("input")) {// no longer temp
						process.getInstructions().add("input");
						process.getInstructions().add(assignInstr[0] + " " + assignInstr[1] + " input");
					} else if (assignInstr[2].equalsIgnoreCase("readFile")) {
						process.getInstructions().add(assignInstr[2] + " " + assignInstr[3]);
						process.getInstructions().add(assignInstr[0] + " " + assignInstr[1] + " readFile");
					} else
						process.getInstructions().add(line);

				} else
					process.getInstructions().add(line);
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return process;

	}

	public void executeInstr(int processID, String instruction) {
		// String instruction = OS.getMemory().getInstruction(processID);
		String[] instructionSplit = instruction.split(" ");
		switch (instructionSplit[0]) {
		case "print":
			Object variableValue = system.SystemCalls.readFromMem(processID, instructionSplit[1]);
			system.SystemCalls.printData(variableValue.toString());
			break;
		case "assign": // assign x y
			if (instructionSplit[2].equalsIgnoreCase("input") || instructionSplit[2].equalsIgnoreCase("readFile"))
				variableValue = OS.getTemp().get(processID);
			else
				variableValue = system.SystemCalls.readFromMem(processID, instructionSplit[2]);
			system.SystemCalls.writeToMem(processID, variableValue, instructionSplit[1]);
			break;
		case "input":
			system.SystemCalls.printData("Please enter a Value");
			Object input = system.SystemCalls.takeInput();
			OS.getTemp().put(processID, input);
//				 v=new Variable("temp",input);
//				 OS.getMemory().setVariable(processID,v);
			break;
		case "writeFile":// writeFile x y
			variableValue = SystemCalls.readFromMem(processID, instructionSplit[2]);
			system.SystemCalls.writeFile((String) SystemCalls.readFromMem(processID, instructionSplit[1]),
					variableValue.toString());
			break;
		case "readFile":// readFile x
			try {
				String file = system.SystemCalls
						.readFile((String) SystemCalls.readFromMem(processID, instructionSplit[1]));
				OS.getTemp().put(processID, file);
				break;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		case "printFromTo":// printFromTo x y
//			int startNumber = Integer.parseInt(instructionSplit[1]);
//			int endNumber = Integer.parseInt(instructionSplit[2]);
			int startNumber = Integer.parseInt((String) SystemCalls.readFromMem(processID, instructionSplit[1]));
			int endNumber = Integer.parseInt((String) SystemCalls.readFromMem(processID, instructionSplit[2]));
			while (startNumber <= endNumber)
				system.SystemCalls.printData(startNumber++);
			break;
		case "semWait":
			Mutex mutex;
			switch (instructionSplit[1]) {
			case "userInput":
				mutex = OS.getUserInput();
				Mutex.semWait(mutex);
				break;
			case "userOutput":
				mutex = OS.getUserOutput();
				Mutex.semWait(mutex);
				break;
			case "file":
				mutex = OS.getFile();
				Mutex.semWait(mutex);
				break;

			}
			break;

		case "semSignal":

			switch (instructionSplit[1]) {
			case "userInput":
				mutex = OS.getUserInput();
				Mutex.semSignal(mutex);
				break;
			case "userOutput":
				mutex = OS.getUserOutput();
				Mutex.semSignal(mutex);
				break;
			case "file":
				mutex = OS.getFile();
				Mutex.semSignal(mutex);
				break;

			}
			break;

		}

	}
}
