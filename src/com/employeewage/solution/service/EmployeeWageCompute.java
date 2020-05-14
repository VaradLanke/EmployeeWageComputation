package com.employeewage.solution.service;
import java.util.*;
import com.employeewage.solution.model.*;

public class EmployeeWageCompute implements EmployeeWageComputeService{

	private List<Company> companyList;
	private Map<String,Company> companyHashMap;

	public EmployeeWageCompute(){
		companyList = new ArrayList<>();
		companyHashMap = new HashMap<>();
	}

	@Override
	public boolean isPresent(Employee emp){
		int randomNumber = new Random().nextInt(100)%2;
		return randomNumber==1 ? true : false;
	}

	public int generateRandomWorkHours(String companyName){
		int fullDayHour = this.findCompany(companyName).getMaxHoursPerMonth();
		return new Random().nextInt(fullDayHour)%2;
	}
	
	@Override
	public void addCompanyWithEmployees(String companyName,int empRatePerHour,int numOfWorkingDays,int maxHoursPerMonth){
		Company cmp = new Company(companyName,empRatePerHour,numOfWorkingDays,maxHoursPerMonth);
		cmp.setEmpList(createDummyEmployee(companyName, numOfWorkingDays));
		companyList.add(cmp);
		companyHashMap.put(companyName, cmp);
		System.out.println("Added Company with emp");
	}

	@Override
	public ArrayList<Employee> createDummyEmployee(String companyName, int numOfWorkingDays){
		ArrayList<Employee> empList = new ArrayList<>();
		Employee e = null;
		for(int i=0;i<10;i++){
			e = new Employee("name"+i, companyName,new ArrayList<Integer>());
			for(int j=0;j<numOfWorkingDays;j++){
				e.getDailyWages().add(computeEmpWage(e,companyName));
			}
			empList.add(e);
		}
		return empList;
	}

	@Override
	public Company findCompany(String companyName){
		for (Company company : companyList) {
			if(company.getCompanyName().equalsIgnoreCase(companyName))
				return company;
		}
		return null;
	}

	@Override
	public void computeEmpWage(){
		for (Company company : companyList) {
			company.setTotalEmpWage(this.computeEmpWage(company));
		}
	}

	//DailyWage Calculation
	public int computeEmpWage(Employee emp, String companyName){
		Company fcmp =  findCompany(companyName);
		int dailyWage = (new Random().nextInt(9) * fcmp.getEmpRatePerHour());
		return dailyWage;
	}

	//totalWageCalculation
	public int computeEmpWage(Company company){
		int totalWage=0,totalWorkingHours=0;
		for (Employee emp : company.getEmpList()) {
			for (int dailyWage : emp.getDailyWages()) {
				if(totalWorkingHours < company.getMaxHoursPerMonth()){
					totalWage += (dailyWage * company.getEmpRatePerHour());
					totalWorkingHours += (totalWorkingHours / company.getEmpRatePerHour());
				}
			}			
		}
		return totalWage;
	}

}