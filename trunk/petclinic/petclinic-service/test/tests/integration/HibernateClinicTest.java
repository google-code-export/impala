package tests.integration;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.impalaframework.module.builder.SimpleModuleDefinitionSource;
import org.impalaframework.module.definition.ModuleDefinitionSource;
import org.impalaframework.module.definition.RootModuleDefinition;
import org.impalaframework.testrun.DynamicContextHolder;
import org.impalaframework.testrun.ImpalaTestRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.samples.petclinic.Clinic;
import org.springframework.samples.petclinic.Owner;
import org.springframework.samples.petclinic.Pet;
import org.springframework.samples.petclinic.PetType;
import org.springframework.samples.petclinic.Specialty;
import org.springframework.samples.petclinic.Vet;
import org.springframework.samples.petclinic.Visit;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.util.FileCopyUtils;

public class HibernateClinicTest extends TestCase implements
		ModuleDefinitionSource {

	public static void main(String[] args) {
		System.setProperty("impala.parent.project", "petclinic");
		ImpalaTestRunner.run(HibernateClinicTest.class);
	}

	private JdbcTemplate jdbcTemplate;

	private Clinic clinic;

	public void setUp() throws Exception {
		super.setUp();
		System.setProperty("impala.parent.project", "petclinic");
		DynamicContextHolder.init(this);
		clinic = DynamicContextHolder.getBean("clinic", Clinic.class);
		jdbcTemplate = new JdbcTemplate(DynamicContextHolder.getBean(
				"dataSource", DataSource.class));

		runScript("../petclinic/db/emptyDB.txt");
		runScript("../petclinic/db/populateDB.txt");
	}

	private void runScript(String file) throws IOException, FileNotFoundException {
		String empty = FileCopyUtils.copyToString(new FileReader(file));
		String[] statements = empty.split(";");
		for (String sql : statements) {
			if (sql.trim().length() > 0) jdbcTemplate.execute(sql);
		}
	}

	/**
	 * This method is provided to set the Clinic instance being tested by the
	 * Dependency Injection injection behaviour of the superclass from the
	 * <code>org.springframework.test</code> package.
	 * 
	 * @param clinic
	 *            clinic to test
	 */
	public void setClinic(Clinic clinic) {
		this.clinic = clinic;
	}

	public void testGetVets() {
		Collection vets = this.clinic.getVets();

		// Use the inherited JdbcTemplate (from
		// AbstractTransactionalDataSourceSpringContextTests)
		// to verify the results of the query
		assertEquals("JDBC query must show the same number of vets",
				jdbcTemplate.queryForInt("SELECT COUNT(0) FROM VETS"), vets
						.size());
		Vet v1 = (Vet) EntityUtils.getById(vets, Vet.class, 2);
		assertEquals("Leary", v1.getLastName());
		assertEquals(1, v1.getNrOfSpecialties());
		assertEquals("radiology", ((Specialty) v1.getSpecialties().get(0))
				.getName());
		Vet v2 = (Vet) EntityUtils.getById(vets, Vet.class, 3);
		assertEquals("Douglas", v2.getLastName());
		assertEquals(2, v2.getNrOfSpecialties());
		assertEquals("dentistry", ((Specialty) v2.getSpecialties().get(0))
				.getName());
		assertEquals("surgery", ((Specialty) v2.getSpecialties().get(1))
				.getName());
	}

	public void testGetPetTypes() {
		Collection petTypes = this.clinic.getPetTypes();
		assertEquals("JDBC query must show the same number of pet typess",
				jdbcTemplate.queryForInt("SELECT COUNT(0) FROM TYPES"),
				petTypes.size());
		PetType t1 = (PetType) EntityUtils.getById(petTypes, PetType.class, 1);
		assertEquals("cat", t1.getName());
		PetType t4 = (PetType) EntityUtils.getById(petTypes, PetType.class, 4);
		assertEquals("snake", t4.getName());
	}

	public void testFindOwners() {
		Collection owners = this.clinic.findOwners("Davis");
		assertEquals(2, owners.size());
		owners = this.clinic.findOwners("Daviss");
		assertEquals(0, owners.size());
	}

	public void testLoadOwner() {
		Owner o1 = this.clinic.loadOwner(1);
		assertTrue(o1.getLastName().startsWith("Franklin"));
		Owner o10 = this.clinic.loadOwner(10);
		assertEquals("Carlos", o10.getFirstName());
	}

	public void testInsertOwner() {
		Collection owners = this.clinic.findOwners("Schultz");
		int found = owners.size();
		Owner owner = new Owner();
		owner.setLastName("Schultz");
		this.clinic.storeOwner(owner);
		owners = this.clinic.findOwners("Schultz");
		assertEquals(found + 1, owners.size());
	}

	public void testUpdateOwner() throws Exception {
		Owner o1 = this.clinic.loadOwner(1);
		String old = o1.getLastName();
		o1.setLastName(old + "X");
		this.clinic.storeOwner(o1);
		o1 = this.clinic.loadOwner(1);
		assertEquals(old + "X", o1.getLastName());
	}

	public void testLoadPet() {
		Collection types = this.clinic.getPetTypes();
		Pet p7 = this.clinic.loadPet(7);
		assertTrue(p7.getName().startsWith("Samantha"));
		assertEquals(EntityUtils.getById(types, PetType.class, 1).getId(), p7
				.getType().getId());
		assertEquals("Jean", p7.getOwner().getFirstName());
		Pet p6 = this.clinic.loadPet(6);
		assertEquals("George", p6.getName());
		assertEquals(EntityUtils.getById(types, PetType.class, 4).getId(), p6
				.getType().getId());
		assertEquals("Peter", p6.getOwner().getFirstName());
	}

	public void testInsertPet() {
		Owner o6 = this.clinic.loadOwner(6);
		int found = o6.getPets().size();
		Pet pet = new Pet();
		pet.setName("bowser");
		Collection types = this.clinic.getPetTypes();
		pet.setType((PetType) EntityUtils.getById(types, PetType.class, 2));
		pet.setBirthDate(new Date());
		o6.addPet(pet);
		assertEquals(found + 1, o6.getPets().size());
		this.clinic.storeOwner(o6);
		o6 = this.clinic.loadOwner(6);
		assertEquals(found + 1, o6.getPets().size());
	}

	public void testUpdatePet() throws Exception {
		Pet p7 = this.clinic.loadPet(7);
		String old = p7.getName();
		p7.setName(old + "X");
		this.clinic.storePet(p7);
		p7 = this.clinic.loadPet(7);
		assertEquals(old + "X", p7.getName());
	}

	public void testInsertVisit() {
		Pet p7 = this.clinic.loadPet(7);
		int found = p7.getVisits().size();
		Visit visit = new Visit();
		p7.addVisit(visit);
		visit.setDescription("test");
		this.clinic.storeVisit(visit);
		System.out.println("end storing pet");
		// assertTrue(!visit.isNew()); -- NOT TRUE FOR TOPLINK (before commit)
		p7 = this.clinic.loadPet(7);
		assertEquals(found + 1, p7.getVisits().size());
	}

	public RootModuleDefinition getModuleDefinition() {

		return new SimpleModuleDefinitionSource(
				new String[] { "parent-context.xml" }, new String[] {
						"petclinic-hibernate", "petclinic-service" })
				.getModuleDefinition();
	}

}
