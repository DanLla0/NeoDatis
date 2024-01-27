
package Principal;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import org.neodatis.odb.*;
import org.neodatis.odb.core.query.IQuery;
import org.neodatis.odb.core.query.criteria.Where;
import org.neodatis.odb.impl.core.query.criteria.CriteriaQuery;
import org.neodatis.odb.impl.core.query.values.ValuesCriteriaQuery;

import Clases.*;

public class Principal {
	private static ODB db;
	private static Connection connection;
	private static final String LINEA = "----------------------------------------------------------------------------------------------------------------";

	public static void main(String[] args) {

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
			connection = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE", "PROYECTOS", "proyectos");

			db = ODBFactory.open("proyectos.dat");
			Scanner t = new Scanner(System.in);
			int option = 0;
			do {
				System.out.println("1.- Crear Base de Datos.");
				System.out.println("2.- Listar Proyectos.");
				System.out.println("3.- Insertar Participaciones.");
				option = t.nextInt();
				switch (option) {
				case 1:
					// 1 - CREAR BD
					crearNeoDatis();
					break;
				case 2:
					// 2 - LISTAR PROYECTO
					listarProyecto(3);
					listarProyecto(25);
					listarProyecto(15);
					break;
				case 3:
					// 3 - INSERTAR PARTICIPACION
					insertarParicipacion(1, 3, "Aportación Benéfica", 100);
					insertarParicipacion(1, 25, "Aportación Benéfica", 1000);
					break;
				default:
					System.out.println("Fin.");
					option = 0;
					break;
				}
			} while (option != 0);

			connection.close();
			db.close();
		} catch (ClassNotFoundException cn) {
			cn.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void insertarParicipacion(int codEstudianteAux, int codProyectoAux, String tipoAportacionAux,
			int numAportacionesAux) throws SQLException {
		if (comprobarEstudiante(codEstudianteAux) && comprobarProyecto(codProyectoAux)) {
			Values maximodb = db
					.getValues(new ValuesCriteriaQuery(Participa.class).max("codparticipacion", "maxCodParticipacion"));
			ObjectValues ov = maximodb.nextValues();
			BigDecimal codParticipacion = (BigDecimal) ov.getByAlias("maxCodParticipacion");
			// int codparticipacion, Estudiantes estudiante, Proyectos proyecto, String
			// tipoparticipacion,int numaportaciones
			Objects<Estudiantes> estudiantesArray = db.getObjects(Estudiantes.class);
			IQuery consulta = new CriteriaQuery(Estudiantes.class, Where.equal("codestudiante", codEstudianteAux));
			Estudiantes estudianteObject = new Estudiantes();
			try {
				estudianteObject = (Estudiantes) db.getObjects(consulta).getFirst();
			} catch (Exception e) {
				System.err.println(
						"No se ha podido insertar participación. El estudiante: " + codEstudianteAux + " no existe.");
			}

			Objects<Proyectos> proyectosArray = db.getObjects(Proyectos.class);
			consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", codProyectoAux));
			Proyectos proyectoObject = new Proyectos();
			try {
				proyectoObject = (Proyectos) db.getObjects(consulta).getFirst();
			} catch (Exception e) {
				System.err.println(
						"No se ha podido insertar participación. El proyecto: " + codProyectoAux + " no existe.");
			}
			Participa participaObject = new Participa((codParticipacion.intValue()) + 1, estudianteObject,
					proyectoObject, tipoAportacionAux, numAportacionesAux);
			proyectoObject.getParticipantes().add(participaObject);
			estudianteObject.getParticipaen().add(participaObject);
			db.store(participaObject);
			db.store(proyectoObject);
			db.store(estudianteObject);
			db.commit();
			System.out.println("La participación: " + participaObject.toString() + " ha sido insertada con éxito.");
		} else
			System.err.println("No se ha podido insertar participación.");

	}

	private static void listarProyecto(int codProyectoAux) {
		try {
			IQuery consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", codProyectoAux));
			Proyectos proyectoObject = (Proyectos) db.getObjects(consulta).getFirst();
			if (comprobarProyecto(codProyectoAux)) {
				String format = "%20s %15s %25s %18s %15s %7s %n";
				System.out.println("Código Proyecto: " + proyectoObject.getCodigoproyecto() + "\t | Nombre Proyecto: "
						+ proyectoObject.getNombre());
				System.out.println("Fecha Inicio: " + proyectoObject.getFechainicio() + "\t | Fecha Fin: "
						+ proyectoObject.getFechafin());
				System.out.println("Presupuesto: " + proyectoObject.getPresupuesto() + "\t | Extraaportación: "
						+ proyectoObject.getExtraaportacion());
				System.out.println(LINEA);
				if (proyectoObject.getParticipantes().size() != 0) {
					System.out.println("Particpantes del proyecto: ");
					System.out.printf(format, "CODPARTICIPACION", "CODESTUDIANTE", "NOMBREESTUDIANTE", "TIPAPORTACION",
							"NUMAPORTACIONES", "IMPORTE");
					System.out.println(LINEA);
					Double total = 0.0d;
					for (Participa participaObject : proyectoObject.getParticipantes()) {
						Double importe = (double) (proyectoObject.getExtraaportacion()
								* participaObject.getNumaportaciones());
						total += importe;
						System.out.printf(format, participaObject.getCodparticipacion(),
								participaObject.getEstudiante().getCodestudiante(),
								participaObject.getEstudiante().getNombre(), participaObject.getTipoparticipacion(),
								participaObject.getNumaportaciones(), importe);
					}
					System.out.println(LINEA);
					// numero de aportaciones + importes
					System.out.println("TOTALES: " + total);
				} else
					System.out.println("NO TIENE PARTICIPANTES.");
			} else {
				System.err.println("EL CÓDIGO DE PROYECTO " + codProyectoAux + " NO EXISTE.");
			}
		} catch (Exception e) {
			System.err.println("EL PROYECTO: " + codProyectoAux + " NO EXISTE.");
		}
		System.out.println();

	}

	private static void insertarParticipacionesEnEstudiantes() {
		try {
			Objects<Estudiantes> objects = db.getObjects(Estudiantes.class);
			while (objects.hasNext()) {
				Estudiantes estudiante = objects.next();
				ArrayList<Participa> setParticipaciones = new ArrayList<Participa>();
				Statement sentencia;

				sentencia = connection.createStatement();

				ResultSet resul = sentencia.executeQuery(
						"SELECT * FROM PARTICIPA where codestudiante = '" + estudiante.getCodestudiante() + "'");
				while (resul.next()) {
					IQuery consulta = new CriteriaQuery(Participa.class,
							Where.equal("codparticipacion", resul.getInt(1)));
					Participa participaObject = (Participa) db.getObjects(consulta).getFirst();
					setParticipaciones.add(participaObject);
				}
				// Asigno el set a la asignatura
				estudiante.setParticipaen(setParticipaciones);
				db.store(estudiante);
				resul.close();
				sentencia.close();
			}
			db.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void insertarParticipacionesEnProyectos() {
		try {
			Objects<Proyectos> objects = db.getObjects(Proyectos.class);
			while (objects.hasNext()) {
				Proyectos proyecto = objects.next();
				ArrayList<Participa> setParticipaciones = new ArrayList<Participa>();
				Statement sentencia;

				sentencia = connection.createStatement();

				ResultSet resul = sentencia.executeQuery(
						"SELECT * FROM PARTICIPA where codigoproyecto = '" + proyecto.getCodigoproyecto() + "'");
				while (resul.next()) {
					IQuery consulta = new CriteriaQuery(Participa.class,
							Where.equal("codparticipacion", resul.getInt(1)));
					Participa participaObject = (Participa) db.getObjects(consulta).getFirst();
					setParticipaciones.add(participaObject);
				}
				// Asigno el set a la asignatura
				proyecto.setParticipantes(setParticipaciones);
				db.store(proyecto);
				resul.close();
				sentencia.close();
			}
			db.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void crearNeoDatis() {
		// TODO Auto-generated method stub
		try {
			insertarEstudiantes();
			insertarProyectos();
			insertarParticipaciones();
			insertarParticipacionesEnEstudiantes();
			insertarParticipacionesEnProyectos();
			System.out.println("Base de datos neodatis creada correctamente");
		} catch (Exception e) {
		}

	}

	private static void insertarParticipaciones() {
		try {
			Statement sentencia = (Statement) connection.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM PARTICIPA");
			while (resul.next()) {
				if (comprobarParticipacion(resul.getInt(1)) == false) {
					int cod = resul.getInt(1);
					IQuery consulta = new CriteriaQuery(Estudiantes.class,
							Where.equal("codestudiante", resul.getInt(2)));
					Estudiantes estudiante = (Estudiantes) db.getObjects(consulta).getFirst();
					consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", resul.getInt(3)));
					Proyectos proyecto = (Proyectos) db.getObjects(consulta).getFirst();
					String tipoParticipacion = resul.getString(4);
					int numAportaciones = resul.getInt(5);
					Participa participaObject = new Participa(cod, estudiante, proyecto, tipoParticipacion,
							numAportaciones);
					db.store(participaObject);
					System.out.println("Participación  grabada: " + resul.getString(1));
				} else
					System.err.println("Participación: " + resul.getInt(1) + ", EXISTE.  NO SE PUEDE INTRODUCIR");
			}
			db.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			System.err.println("Error insertando participaciones.");

		}

	}

	private static void insertarProyectos() {
		try {
			Statement sentencia = (Statement) connection.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM PROYECTOS");
			while (resul.next()) {
				if (comprobarProyecto(resul.getInt(1)) == false) {
					HashSet<Proyectos> setProyectos = new HashSet<Proyectos>();
					int cod = resul.getInt(1);
					String nombre = resul.getString(2);
					Date fechainicio = resul.getDate(3);
					Date fechafin = resul.getDate(4);
					Float presupuesto = resul.getFloat(5);
					Float extraaportacion = resul.getFloat(6);
					Proyectos proyectoObject = new Proyectos(cod, nombre, fechainicio, fechafin, presupuesto,
							extraaportacion);
					db.store(proyectoObject);
					System.out.println("Proyecto grabado: " + resul.getString(1));
				} else
					System.err.println("Proyecto: " + resul.getInt(1) + ", NO EXISTE.  NO SE PUEDE INTRODUCIR");

			}
			db.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void insertarEstudiantes() {
		try {
			Statement sentencia = (Statement) connection.createStatement();
			ResultSet resul = sentencia.executeQuery("SELECT * FROM ESTUDIANTES");
			while (resul.next()) {
				if (comprobarEstudiante(resul.getInt(1)) == false) {
					HashSet<Estudiantes> setEstudiantes = new HashSet<Estudiantes>();
					int cod = resul.getInt(1);
					String nombre = resul.getString(2);
					String direccion = resul.getString(3);
					String tlf = resul.getString(4);
					Date fechaalta = resul.getDate(5);
					Estudiantes estudiateObject = new Estudiantes(cod, nombre, direccion, tlf, fechaalta);
					db.store(estudiateObject);
					System.out.println("Estudiante grabado: " + resul.getString(1));
				} else
					System.err
							.println("Estudiante: " + resul.getInt(1) + ", EXISTE EN NEODATIS. NO SE PUEDE INTRODUCIR");

			}
			db.commit();
			resul.close();
			sentencia.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static boolean comprobarEstudiante(int codEstudianteAux) {
		try {
			IQuery consulta = new CriteriaQuery(Estudiantes.class, Where.equal("codestudiante", codEstudianteAux));
			Estudiantes estudianteObject = (Estudiantes) db.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {

			return false;
		}
	}

	private static boolean comprobarParticipacion(int codParticipacionAux) {
		try {
			IQuery consulta = new CriteriaQuery(Participa.class, Where.equal("codparticipacion", codParticipacionAux));
			Participa participacionObject = (Participa) db.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {
			return false;
		}
	}

	private static boolean comprobarProyecto(int codProyectoAux) {
		try {
			IQuery consulta = new CriteriaQuery(Proyectos.class, Where.equal("codigoproyecto", codProyectoAux));
			Proyectos proyectoObject = (Proyectos) db.getObjects(consulta).getFirst();
			return true;
		} catch (IndexOutOfBoundsException e) {

			return false;
		}
	}
}
