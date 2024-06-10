// 2024 | Mehran Nosrati

package SobaZino;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.cloudbus.cloudsim.Cloudlet;
import org.cloudbus.cloudsim.CloudletSchedulerTimeShared;
import org.cloudbus.cloudsim.Datacenter;
import org.cloudbus.cloudsim.DatacenterBroker;
import org.cloudbus.cloudsim.DatacenterCharacteristics;
import org.cloudbus.cloudsim.Host;
import org.cloudbus.cloudsim.Log;
import org.cloudbus.cloudsim.Pe;
import org.cloudbus.cloudsim.Storage;
import org.cloudbus.cloudsim.Vm;
import org.cloudbus.cloudsim.VmAllocationPolicySimple;
import org.cloudbus.cloudsim.VmSchedulerTimeShared;
import org.cloudbus.cloudsim.core.CloudSim;
import org.cloudbus.cloudsim.provisioners.BwProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.PeProvisionerSimple;
import org.cloudbus.cloudsim.provisioners.RamProvisionerSimple;
import org.cloudbus.cloudsim.util.WorkloadFileReader;

public class Simulation {

	private static Datacenter[] datacenter;
	private static List<Cloudlet> cloudletList;
	private static List<Vm> vmlist;

	private static List<Vm> createVM(int userId, int vms) {
		LinkedList<Vm> list = new LinkedList<Vm>();
		
		long size = Constants.VM_SIZE;
		int ram = Constants.VM_RAM;
		long bw = Constants.VM_BW;
		int pesNumber = Constants.NO_PESNUMBER;
		String vmm = Constants.VM_VMM;

		for(int i=0;i<vms;i++){
			list.add(new Vm(i, userId, Constants.VM_MIPS[i % Constants.VM_MIPS.length], pesNumber, ram, bw, size, vmm, new CloudletSchedulerTimeShared()));
		}

		return list;
	}
	
	private static List<Cloudlet> createCloudlet(int brokerId, String filedir, int startIndex, int finishIndex) throws FileNotFoundException{
		List<Cloudlet> cloudletList;

		WorkloadFileReader workloadFileReader = new WorkloadFileReader(filedir, 1);
		cloudletList = workloadFileReader.generateWorkload().subList(startIndex, finishIndex);

        for (Cloudlet cloudlet : cloudletList)
            cloudlet.setUserId(brokerId);

		return cloudletList;
	}
	
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		DatacenterBroker.CTM = new double[Constants.NO_OF_TASKS][Constants.NO_OF_VMS];
		DatacenterBroker.TASK = new double[Constants.NO_OF_TASKS][2];
		Log.printLine("Start Simulation...");
		try {
			int num_user = Constants.NO_OF_USER;
			Calendar calendar = Calendar.getInstance();
			boolean trace_flag = false;

			CloudSim.init(num_user, calendar, trace_flag);
			
			datacenter = new Datacenter[Constants.NO_OF_DATA_CENTERS];
            for (int i = 0; i < Constants.NO_OF_DATA_CENTERS; i++) {
                datacenter[i] = createDatacenter("Datacenter_" + i);
            }

			DatacenterBroker broker = createBroker();
			int brokerId = broker.getId();

			vmlist = createVM(brokerId,Constants.NO_OF_VMS);
			cloudletList = createCloudlet(brokerId, "D:\\Program\\eclipse\\Project\\cloudsim-3.0.3\\Project\\SobaZino\\HPC2N-2002-2.2-cln.swf", 0 , Constants.NO_OF_TASKS);

			broker.submitVmList(vmlist);
			broker.submitCloudletList(cloudletList);

			CloudSim.startSimulation();
			
			List<Cloudlet> newList = broker.getCloudletReceivedList();
			
			CloudSim.stopSimulation();

			printCloudletList(newList);

			Log.printLine("\n========== Mehran Nosrati | 2024 ========== finished!");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.printLine("The simulation has been terminated due to an unexpected error");
		}
	}

	public static Datacenter createDatacenter(String name) {
        List<Host> hostList = new ArrayList<Host>();

        int mips = Constants.DC_MIPS;
        int ram = Constants.DC_RAM;
        long storage = Constants.DC_STORAGE;
        int bw = Constants.DC_BW;

        for (int i = 0; i < Constants.DC_HOST; i++) {
            List<Pe> peList = new ArrayList<Pe>();
            for (int j = 0; j <= Constants.DC_CPU; j++) {
                peList.add(new Pe(j, new PeProvisionerSimple(mips)));
            }
            hostList.add( new Host( i, new RamProvisionerSimple(ram), new BwProvisionerSimple(bw), storage, peList, new VmSchedulerTimeShared(peList) ) );
        }

        String arch = Constants.DC_ARCH;
        String os = Constants.DC_OS;
        String vmm = Constants.DC_VMM;
        double time_zone = Constants.DC_TIMEZONE;
        double cost = Constants.DC_COST;
        double costPerMem = Constants.DC_COSTPERMEM;
        double costPerStorage = Constants.DC_COSTPERSTORAGE;
        double costPerBw = Constants.DC_COSTPERBW;
        LinkedList<Storage> storageList = new LinkedList<Storage>();

        DatacenterCharacteristics characteristics = new DatacenterCharacteristics(
                arch, os, vmm, hostList, time_zone, cost, costPerMem, costPerStorage, costPerBw);

        Datacenter datacenter = null;
        try {
            datacenter = new Datacenter(name, characteristics, new VmAllocationPolicySimple(hostList), storageList, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return datacenter;
    }

	private static DatacenterBroker createBroker(){

		DatacenterBroker broker = null;
		try {
			broker = new DatacenterBroker("Broker");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return broker;
	}
	
	private static void printCloudletList(List<Cloudlet> list) {
		int size = list.size();
		double AT = 0;
		Cloudlet cloudlet;

		String indent = "    ";
		Log.printLine();
		Log.printLine("========== OUTPUT ==========");
		Log.printLine("Cloudlet ID" + indent + "STATUS" + indent +
				"Data center ID" + indent + "VM ID" + indent + indent + "Time" + indent + "Start Time" + indent + "Finish Time" + indent + "Completion Time");

		for (int i = 0; i < size; i++) {
			cloudlet = list.get(i);
			Log.print(indent + cloudlet.getCloudletId() + indent + indent);

			if (cloudlet.getCloudletStatus() == Cloudlet.SUCCESS){
				double CT = 0;
				for (int j = 0; j < size; j++) {
					if (DatacenterBroker.TASK[j][0] == cloudlet.getCloudletId()) {
						CT = DatacenterBroker.TASK[j][1];
						break;
					}
				}
				Log.print("SUCCESS");

				Log.printLine( indent + indent + cloudlet.getResourceId() + indent + indent + indent + cloudlet.getVmId() +
						indent + indent + indent + cloudlet.getActualCPUTime() +
						indent + indent + cloudlet.getExecStartTime() + indent + indent + indent + cloudlet.getFinishTime() + indent + indent + indent + CT);
				AT += CT - cloudlet.getExecStartTime();
			}
		}
		
		double Makespan = 0;
		double TotalMakespan = 0;
		double Total_Avg_RT = 0;
		for (int i = 0; i< Constants.NO_OF_VMS; i++) {
			int TaskSize = 0;
			double Total_RT = 0;
			double VMMakespan = 0;
			for (int j = 0; j< Constants.NO_OF_TASKS; j++) {
				double CT = DatacenterBroker.CTM[j][i];
				if (CT > VMMakespan) {
					VMMakespan = CT;
					Makespan = CT;
				}
				if (CT > 0) {
					TaskSize++;
					double SB = 0;
					for (int k = 0; k < size; k++) {
						cloudlet = list.get(k);
						if (DatacenterBroker.TASK[j][0] == cloudlet.getCloudletId()) {
							SB = cloudlet.getExecStartTime();
						}
					}
					Total_RT += Math.abs(CT - SB);
				}
			}
			TotalMakespan += VMMakespan;
			Total_Avg_RT += Total_RT / TaskSize;
		}
		
		double AvgMakespan = TotalMakespan / Constants.NO_OF_VMS;
		Log.printLine("------- Avg Makespan: " + AvgMakespan);
		
		double M_Avg_RT = Total_Avg_RT / Constants.NO_OF_VMS;
		Log.printLine("------- Mean of total average response time: " + M_Avg_RT);
		
		Log.printLine("------- Actual Execution Time: " + AT);
		
		double RU = TotalMakespan / (Constants.NO_OF_VMS * Makespan);
		Log.printLine("------- Avg Resource Utilization: " + RU);
		
		double Throughput = list.size() / Makespan;
		Log.printLine("------- Throughput: " + Throughput);
	}
}