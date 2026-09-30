package backend_equipo_bravo.analisis_sistema_II.service;

import backend_equipo_bravo.analisis_sistema_II.dto.ControlMotivoEmpleado;
import backend_equipo_bravo.analisis_sistema_II.dto.ControlStatusEmpleado;
import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.EmpleadoBaseDto;
import backend_equipo_bravo.analisis_sistema_II.dto.liquidacion.LiquidacionDto;
import backend_equipo_bravo.analisis_sistema_II.entity.Empleado;
import backend_equipo_bravo.analisis_sistema_II.entity.Liquidacion;
import backend_equipo_bravo.analisis_sistema_II.entity.serializables.FlujoStatusEmpleadoId;
import backend_equipo_bravo.analisis_sistema_II.exception.BusinessException;
import backend_equipo_bravo.analisis_sistema_II.exception.errorCode.GeneralError;
import backend_equipo_bravo.analisis_sistema_II.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class LiquidacionService extends BaseService<Liquidacion, Integer> {

    @Autowired
    private LiquidacionRepository liquidacionRepository;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private PersonaRepository personaRepository;
    @Autowired
    private PuestoRepository puestoRepository;
    @Autowired
    private DepartamentoRepository departamentoRepository;
    @Autowired
    private StatusEmpleadoRepository statusEmpleadoRepository;
    @Autowired
    private FlujoStatusEmpleadoRepository flujoStatusEmpleadoRepository;
    @Autowired
    private CatalogoService catalogoService;

    @Override
    protected JpaRepository<Liquidacion, Integer> getRepository() {
        return liquidacionRepository;
    }

    @Override
    protected RuntimeException getNotFoundException(Integer id) {
        return new BusinessException(GeneralError.ERROR_SERVICE);
    }

    public EmpleadoBaseDto obtenerEmpleadoBase(Integer idEmpleado) {
        validarConsulta();
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));
        return mapearAEmpleadoBaseDto(empleado);
    }

    public List<EmpleadoBaseDto> listarEmpleadosBase() {
        validarConsulta();
        return empleadoRepository.findAll().stream()
                .map(this::mapearAEmpleadoBaseDto)
                .collect(Collectors.toList());
    }

    public LiquidacionDto obtenerPorEmpleado(Integer idEmpleado) {
        validarConsulta();
        Empleado empleado = empleadoRepository.findById(idEmpleado)
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        Optional<Liquidacion> liquidacionOpt = liquidacionRepository
                .findByIdEmpleadoAndFechaContratacion(idEmpleado, empleado.getFechaContratacion());

        if (liquidacionOpt.isPresent()) {
            return mapearADto(liquidacionOpt.get());
        }

        LiquidacionDto dto = new LiquidacionDto();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
        if (empleado.getIdStatusEmpleado() != null) {
            statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                    dto.setNombreStatus(status.getNombre())
            );
        }
        dto.setIdPuesto(empleado.getIdPuesto());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        dto.setIngresoSueldoBase(empleado.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(empleado.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(empleado.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(empleado.getDescuentoIgss());
        dto.setDescuentoIsr(empleado.getDescuentoIsr());
        dto.setDescuentoInasistencias(empleado.getDescuentoInasistencias());

        personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
        );

        puestoRepository.findById(empleado.getIdPuesto()).ifPresent(puesto -> {
            dto.setNombrePuesto(puesto.getNombre());
            dto.setIdDepartamento(puesto.getIdDepartamento());
            if (puesto.getIdDepartamento() != null) {
                departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                        dto.setNombreDepartamento(dep.getNombre())
                );
            }
        });

        return dto;
    }

    public List<LiquidacionDto> obtenerHistorialPorEmpleado(Integer idEmpleado) {
        validarConsulta();
        return liquidacionRepository.findByIdEmpleado(idEmpleado).stream()
                .map(this::mapearADto)
                .collect(Collectors.toList());
    }

    @Transactional
    public LiquidacionDto procesarLiquidacion(LiquidacionDto request) {
        if (request.getIdEmpleado() == null) {
            throw new BusinessException(GeneralError.ERROR_SERVICE);
        }

        boolean esNuevo = request.getIdLiquidacion() == null;

        if (esNuevo) {
            validarAlta();
        } else {
            validarCambio();
        }

        Empleado empleado = empleadoRepository.findById(request.getIdEmpleado())
                .orElseThrow(() -> new BusinessException(GeneralError.ERROR_EMPLEADO_NOT_FOUND));

        Liquidacion liquidacion;
        if (esNuevo) {
            liquidacion = new Liquidacion();
            liquidacion.setFechaCreacion(LocalDateTime.now());
            liquidacion.setUsuarioCreacion(obtenerUsuarioAutenticado());
        } else {
            liquidacion = liquidacionRepository.findById(request.getIdLiquidacion())
                    .orElseThrow(() -> new BusinessException(GeneralError.ERROR_SERVICE));
        }

        LocalDate fechaContratacion = request.getFechaContratacion() != null ? request.getFechaContratacion() : empleado.getFechaContratacion();
        LocalDate fechaEgreso = request.getFechaEgreso() != null ? request.getFechaEgreso() : LocalDate.now();

        if (fechaContratacion != null && fechaEgreso != null) {
            if (fechaEgreso.isBefore(fechaContratacion)) {
                throw new BusinessException(GeneralError.RANGO_FECHAS_INVALIDO);
            }
        }

        Integer nuevoStatus = request.getIdStatusEmpleado() != null ? request.getIdStatusEmpleado() : ControlStatusEmpleado.BAJA.getId();
        
        String motivoEgresoStr = request.getMotivoEgreso() != null ? request.getMotivoEgreso().trim() : "";
        boolean esDespidoMotivo = String.valueOf(ControlMotivoEmpleado.DESPIDO.getId()).equals(motivoEgresoStr);
        AtomicReference<String> motivoEgresoDB = new AtomicReference<>("Renuncia");


        catalogoService.getMotivosEgreso().stream()
                .filter(item -> item.getCodigo().toString().equals(String.valueOf(request.getMotivoEgreso())))
                .findFirst()
                .ifPresent(item -> motivoEgresoDB.set(item.getValor()));

        if (esDespidoMotivo && request.getIdStatusEmpleado() == null) {
            nuevoStatus = ControlStatusEmpleado.DESPEDIDO.getId();
        }

        liquidacion.setIdEmpleado(empleado.getIdEmpleado());
        liquidacion.setFechaContratacion(fechaContratacion);
        liquidacion.setFechaEgreso(fechaEgreso);
        liquidacion.setFechaLiquidacion(LocalDate.now());
        liquidacion.setMotivoEgreso(motivoEgresoDB.get());
        liquidacion.setIdPuesto(request.getIdPuesto() != null ? request.getIdPuesto() : empleado.getIdPuesto());

        // 1. SET THE RAW BASE COLUMNS EXACTLY AS TYPED IN THE FORM
        liquidacion.setIngresoSueldoBase(valorOZero(request.getIngresoSueldoBase()));
        liquidacion.setIngresoBonificacionDecreto(valorOZero(request.getIngresoBonificacionDecreto()));
        liquidacion.setIngresoOtrosIngresos(valorOZero(request.getIngresoOtrosIngresos()));
        liquidacion.setDescuentoIgss(valorOZero(request.getDescuentoIgss()));
        liquidacion.setDescuentoIsr(valorOZero(request.getDescuentoIsr()));
        liquidacion.setDescuentoInasistencias(valorOZero(request.getDescuentoInasistencias()));

        boolean calcSalario = true;
        if (request.getCalcularSalarioPendiente() != null) {
            calcSalario = request.getCalcularSalarioPendiente().toString().equalsIgnoreCase("true");
        }
        boolean calcAguinaldo = true;
        if (request.getCalcularAguinaldo() != null) {
            calcAguinaldo = request.getCalcularAguinaldo().toString().equalsIgnoreCase("true");
        }
        boolean calcBono14 = true;
        if (request.getCalcularBono14() != null) {
            calcBono14 = request.getCalcularBono14().toString().equalsIgnoreCase("true");
        }
        boolean calcVacaciones = true;
        if (request.getCalcularVacaciones() != null) {
            calcVacaciones = request.getCalcularVacaciones().toString().equalsIgnoreCase("true");
        }
        
        boolean flagManualIndemnizacion = request.getCalcularIndemnizacion() != null && request.getCalcularIndemnizacion().toString().equalsIgnoreCase("true");
        boolean calcIndemnizacion = esDespidoMotivo && flagManualIndemnizacion;

        LiquidacionDto calculosTemporales = calcularDesglose(liquidacion.getIngresoSueldoBase(), fechaContratacion, fechaEgreso, calcIndemnizacion, calcSalario, calcAguinaldo, calcBono14, calcVacaciones);
        
        BigDecimal montoSueldoParaTotal = calcSalario ? calculosTemporales.getMontoSalarioPendiente() : liquidacion.getIngresoSueldoBase();
        BigDecimal montoBonoParaTotal = calcSalario ? calculosTemporales.getMontoBonificacionDecretoPendiente() : liquidacion.getIngresoBonificacionDecreto();
        BigDecimal montoIgssParaTotal = calcSalario ? calculosTemporales.getDescuentoIgss() : liquidacion.getDescuentoIgss();

        BigDecimal prestaciones = calculosTemporales.getMontoIndemnizacion()
                .add(calculosTemporales.getMontoAguinaldo())
                .add(calculosTemporales.getMontoBono14())
                .add(calculosTemporales.getMontoVacaciones());

        BigDecimal totalIngresos = montoSueldoParaTotal
                .add(montoBonoParaTotal)
                .add(liquidacion.getIngresoOtrosIngresos())
                .add(prestaciones);

        BigDecimal totalDescuentos = montoIgssParaTotal
                .add(liquidacion.getDescuentoIsr())
                .add(liquidacion.getDescuentoInasistencias());

        BigDecimal salarioNeto = totalIngresos.subtract(totalDescuentos);

        liquidacion.setTotalIngresos(totalIngresos);
        liquidacion.setTotalDescuentos(totalDescuentos);
        liquidacion.setSalarioNeto(salarioNeto);
        liquidacion.setTotalNeto(salarioNeto);

        // Actualizar Status
        Integer actualStatus = empleado.getIdStatusEmpleado();
        if (actualStatus != null && !actualStatus.equals(nuevoStatus)) {
            boolean transicionValida = flujoStatusEmpleadoRepository.existsById(new FlujoStatusEmpleadoId(actualStatus, nuevoStatus));
            if (!transicionValida && flujoStatusEmpleadoRepository.count() > 0) {
                throw new BusinessException(GeneralError.TRANSICION_STATUS_INVALIDA);
            }
            empleado.setIdStatusEmpleado(nuevoStatus);
            empleado.setFechaModificacion(LocalDateTime.now());
            empleado.setUsuarioModificacion(obtenerUsuarioAutenticado());
            empleadoRepository.save(empleado);
        }

        return mapearADto(crearBase(liquidacion));
    }



    private LiquidacionDto calcularDesglose(BigDecimal salarioBase, LocalDate contratacion, LocalDate egreso, boolean calcIndemnizacion, boolean calcSalario, boolean calcAguinaldo, boolean calcBono14, boolean calcVacaciones) {
        LiquidacionDto dto = new LiquidacionDto();
        
        int diasTotales = (int) ChronoUnit.DAYS.between(contratacion, egreso) + 1;
        if (diasTotales < 0) diasTotales = 0;
        dto.setDiasLaboradosTotal(diasTotales);

        // Indemnización
        BigDecimal salarioPromedioIndemnizacion = salarioBase.multiply(new BigDecimal("14")).divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
        BigDecimal montoIndemnizacion = BigDecimal.ZERO;
        if (calcIndemnizacion) {
            montoIndemnizacion = salarioPromedioIndemnizacion.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasTotales)).setScale(2, RoundingMode.HALF_UP);
        }
        dto.setMontoIndemnizacion(montoIndemnizacion);

        // Aguinaldo
        BigDecimal montoAguinaldo = BigDecimal.ZERO;
        int diasAguinaldo = 0;
        if (calcAguinaldo) {
            LocalDate inicioAguinaldo = egreso.getMonthValue() == 12 ? LocalDate.of(egreso.getYear(), 12, 1) : LocalDate.of(egreso.getYear() - 1, 12, 1);
            if (inicioAguinaldo.isBefore(contratacion)) inicioAguinaldo = contratacion;
            diasAguinaldo = (int) ChronoUnit.DAYS.between(inicioAguinaldo, egreso) + 1;
            if(diasAguinaldo < 0) diasAguinaldo = 0;
            montoAguinaldo = salarioBase.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasAguinaldo)).setScale(2, RoundingMode.HALF_UP);
        }
        dto.setDiasProporcionalesAguinaldo(diasAguinaldo);
        dto.setMontoAguinaldo(montoAguinaldo);

        // Bono 14
        BigDecimal montoBono14 = BigDecimal.ZERO;
        int diasBono14 = 0;
        if (calcBono14) {
            LocalDate inicioBono14 = egreso.getMonthValue() >= 7 ? LocalDate.of(egreso.getYear(), 7, 1) : LocalDate.of(egreso.getYear() - 1, 7, 1);
            if (inicioBono14.isBefore(contratacion)) inicioBono14 = contratacion;
            diasBono14 = (int) ChronoUnit.DAYS.between(inicioBono14, egreso) + 1;
            if(diasBono14 < 0) diasBono14 = 0;
            montoBono14 = salarioBase.divide(new BigDecimal("365"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasBono14)).setScale(2, RoundingMode.HALF_UP);
        }
        dto.setDiasProporcionalesBono14(diasBono14);
        dto.setMontoBono14(montoBono14);

        // Vacaciones
        BigDecimal montoVacaciones = BigDecimal.ZERO;
        int diasVacaciones = 0;
        if (calcVacaciones) {
            int aniosCompletos = Period.between(contratacion, egreso).getYears();
            LocalDate ultimoAniversario = contratacion.plusYears(aniosCompletos);
            diasVacaciones = (int) ChronoUnit.DAYS.between(ultimoAniversario, egreso) + 1;
            if(diasVacaciones < 0) diasVacaciones = 0;
            montoVacaciones = salarioBase.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("15")).multiply(new BigDecimal(diasVacaciones)).divide(new BigDecimal("365"), 2, RoundingMode.HALF_UP);
        }
        dto.setDiasProporcionalesVacaciones(diasVacaciones);
        dto.setMontoVacaciones(montoVacaciones);

        // Salario Pendiente
        BigDecimal montoSalarioPendiente = BigDecimal.ZERO;
        BigDecimal montoBonoDecreto = BigDecimal.ZERO;
        BigDecimal descuentoIgss = BigDecimal.ZERO;
        int diasPendientes = Math.min(egreso.getDayOfMonth(), 30);
        if (calcSalario) {
            montoSalarioPendiente = salarioBase.divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasPendientes)).setScale(2, RoundingMode.HALF_UP);
            montoBonoDecreto = new BigDecimal("250").divide(new BigDecimal("30"), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(diasPendientes)).setScale(2, RoundingMode.HALF_UP);
            descuentoIgss = montoSalarioPendiente.multiply(new BigDecimal("0.0483")).setScale(2, RoundingMode.HALF_UP);
        }
        dto.setDiasPendientesPago(diasPendientes);
        dto.setMontoSalarioPendiente(montoSalarioPendiente);
        dto.setMontoBonificacionDecretoPendiente(montoBonoDecreto);
        dto.setDescuentoIgss(descuentoIgss);

        return dto;
    }

    private BigDecimal valorOZero(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    public List<LiquidacionDto> obtenerTodas() {
        validarConsulta();
        return buscarTodos().stream().map(this::mapearADto).collect(Collectors.toList());
    }

    public LiquidacionDto obtenerPorIdLiquidacion(Integer id) {
        validarConsulta();
        return mapearADto(buscarPorId(id));
    }

    private LiquidacionDto mapearADto(Liquidacion entidad) {
        LiquidacionDto dto = new LiquidacionDto();
        dto.setIdLiquidacion(entidad.getIdLiquidacion());
        dto.setIdEmpleado(entidad.getIdEmpleado());
        dto.setFechaContratacion(entidad.getFechaContratacion());
        dto.setFechaEgreso(entidad.getFechaEgreso());
        dto.setFechaLiquidacion(entidad.getFechaLiquidacion());
        dto.setMotivoEgreso(entidad.getMotivoEgreso());
        dto.setIdPuesto(entidad.getIdPuesto());

        dto.setIngresoSueldoBase(entidad.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(entidad.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(entidad.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(entidad.getDescuentoIgss());
        dto.setDescuentoIsr(entidad.getDescuentoIsr());
        dto.setDescuentoInasistencias(entidad.getDescuentoInasistencias());

        dto.setSalarioNeto(entidad.getSalarioNeto());
        dto.setTotalIngresos(entidad.getTotalIngresos());
        dto.setTotalDescuentos(entidad.getTotalDescuentos());
        dto.setTotalNeto(entidad.getTotalNeto());
        dto.setFechaCreacion(entidad.getFechaCreacion());

        boolean esDespido = false;
        Optional<Empleado> empOpt = empleadoRepository.findById(entidad.getIdEmpleado());
        if (empOpt.isPresent() && empOpt.get().getIdStatusEmpleado() != null) {
            esDespido = empOpt.get().getIdStatusEmpleado().equals(ControlStatusEmpleado.DESPEDIDO.getId());
        }

        if (entidad.getFechaContratacion() != null && entidad.getFechaEgreso() != null && entidad.getIngresoSueldoBase().compareTo(BigDecimal.ZERO) > 0) {
            LiquidacionDto desglose = calcularDesglose(entidad.getIngresoSueldoBase(), entidad.getFechaContratacion(), entidad.getFechaEgreso(), true, true, true, true, true);
            
            // Adivinar la combinación exacta de banderas (true/false) que produjo este TotalIngresos
            BigDecimal valSalarioTrue = valorOZero(desglose.getMontoSalarioPendiente()).add(valorOZero(desglose.getMontoBonificacionDecretoPendiente()));
            BigDecimal valSalarioFalse = valorOZero(entidad.getIngresoSueldoBase()).add(valorOZero(entidad.getIngresoBonificacionDecreto()));
            BigDecimal valOtros = valorOZero(entidad.getIngresoOtrosIngresos());
            
            BigDecimal valIndemnizacion = valorOZero(desglose.getMontoIndemnizacion());
            BigDecimal valAguinaldo = valorOZero(desglose.getMontoAguinaldo());
            BigDecimal valBono14 = valorOZero(desglose.getMontoBono14());
            BigDecimal valVacaciones = valorOZero(desglose.getMontoVacaciones());

            BigDecimal totalDB = valorOZero(entidad.getTotalIngresos());
            
            boolean cSal = true, cInd = true, cAgu = true, cBon = true, cVac = true;
            boolean found = false;

            for(int i = 0; i < 32; i++) {
                boolean tSal = (i & 1) != 0;
                boolean tInd = (i & 2) != 0;
                boolean tAgu = (i & 4) != 0;
                boolean tBon = (i & 8) != 0;
                boolean tVac = (i & 16) != 0;
                
                BigDecimal sum = valOtros.add(tSal ? valSalarioTrue : valSalarioFalse);
                if (tInd) sum = sum.add(valIndemnizacion);
                if (tAgu) sum = sum.add(valAguinaldo);
                if (tBon) sum = sum.add(valBono14);
                if (tVac) sum = sum.add(valVacaciones);
                
                if (sum.subtract(totalDB).abs().compareTo(new BigDecimal("1.00")) <= 0) {
                    cSal = tSal; cInd = tInd; cAgu = tAgu; cBon = tBon; cVac = tVac;
                    found = true; break;
                }
            }

            // Si por alguna razón matemática no cuadra exacto, asumimos que todas están en false (puros crudos del form)
            if (!found) {
                cSal = false; cInd = false; cAgu = false; cBon = false; cVac = false;
            }

            // Aplicamos los resultados deducidos al desglose final
            if (!cSal) {
                desglose.setMontoSalarioPendiente(entidad.getIngresoSueldoBase());
                desglose.setMontoBonificacionDecretoPendiente(entidad.getIngresoBonificacionDecreto());
                desglose.setDescuentoIgss(entidad.getDescuentoIgss());
                // No anulamos los días, mantenemos la variable procesada (topada a 30)
            }
            if (!cInd) { desglose.setMontoIndemnizacion(BigDecimal.ZERO); }
            if (!cAgu) { desglose.setMontoAguinaldo(BigDecimal.ZERO); desglose.setDiasProporcionalesAguinaldo(0); }
            if (!cBon) { desglose.setMontoBono14(BigDecimal.ZERO); desglose.setDiasProporcionalesBono14(0); }
            if (!cVac) { desglose.setMontoVacaciones(BigDecimal.ZERO); desglose.setDiasProporcionalesVacaciones(0); }

            dto.setDiasLaboradosTotal(desglose.getDiasLaboradosTotal());
            dto.setMontoIndemnizacion(desglose.getMontoIndemnizacion());
            dto.setDiasProporcionalesAguinaldo(desglose.getDiasProporcionalesAguinaldo());
            dto.setMontoAguinaldo(desglose.getMontoAguinaldo());
            dto.setDiasProporcionalesBono14(desglose.getDiasProporcionalesBono14());
            dto.setMontoBono14(desglose.getMontoBono14());
            dto.setDiasProporcionalesVacaciones(desglose.getDiasProporcionalesVacaciones());
            dto.setMontoVacaciones(desglose.getMontoVacaciones());
            dto.setDiasPendientesPago(desglose.getDiasPendientesPago());
            
            // Usamos las variables en DTO transient solo para el renderizado PDF
            dto.setMontoSalarioPendiente(desglose.getMontoSalarioPendiente());
            dto.setMontoBonificacionDecretoPendiente(desglose.getMontoBonificacionDecretoPendiente());
        }

        if (empOpt.isPresent()) {
            Empleado empleado = empOpt.get();
            dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
            if (empleado.getIdStatusEmpleado() != null) {
                statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                        dto.setNombreStatus(status.getNombre())
                );
            }
            personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                    dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
            );
        }

        if (entidad.getIdPuesto() != null) {
            puestoRepository.findById(entidad.getIdPuesto()).ifPresent(puesto -> {
                dto.setNombrePuesto(puesto.getNombre());
                dto.setIdDepartamento(puesto.getIdDepartamento());
                if (puesto.getIdDepartamento() != null) {
                    departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                            dto.setNombreDepartamento(dep.getNombre())
                    );
                }
            });
        }

        return dto;
    }

    private EmpleadoBaseDto mapearAEmpleadoBaseDto(Empleado empleado) {
        EmpleadoBaseDto dto = new EmpleadoBaseDto();
        dto.setIdEmpleado(empleado.getIdEmpleado());
        dto.setIdPersona(empleado.getIdPersona());
        dto.setIdSucursal(empleado.getIdSucursal());
        dto.setIdPuesto(empleado.getIdPuesto());
        dto.setIdStatusEmpleado(empleado.getIdStatusEmpleado());
        dto.setFechaContratacion(empleado.getFechaContratacion());
        dto.setIngresoSueldoBase(empleado.getIngresoSueldoBase());
        dto.setIngresoBonificacionDecreto(empleado.getIngresoBonificacionDecreto());
        dto.setIngresoOtrosIngresos(empleado.getIngresoOtrosIngresos());
        dto.setDescuentoIgss(empleado.getDescuentoIgss());
        dto.setDescuentoIsr(empleado.getDescuentoIsr());
        dto.setDescuentoInasistencias(empleado.getDescuentoInasistencias());

        if (empleado.getIdPersona() != null) {
            personaRepository.findById(empleado.getIdPersona()).ifPresent(persona ->
                    dto.setNombreEmpleado(persona.getNombre() + " " + persona.getApellido())
            );
        }

        if (empleado.getIdPuesto() != null) {
            puestoRepository.findById(empleado.getIdPuesto()).ifPresent(puesto -> {
                dto.setNombrePuesto(puesto.getNombre());
                dto.setIdDepartamento(puesto.getIdDepartamento());
                if (puesto.getIdDepartamento() != null) {
                    departamentoRepository.findById(puesto.getIdDepartamento()).ifPresent(dep ->
                            dto.setNombreDepartamento(dep.getNombre())
                    );
                }
            });
        }

        if (empleado.getIdStatusEmpleado() != null) {
            statusEmpleadoRepository.findById(empleado.getIdStatusEmpleado()).ifPresent(status ->
                    dto.setNombreStatus(status.getNombre())
            );
        }

        return dto;
    }
}