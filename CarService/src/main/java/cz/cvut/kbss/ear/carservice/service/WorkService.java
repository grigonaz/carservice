package cz.cvut.kbss.ear.carservice.service;

import cz.cvut.kbss.ear.carservice.dao.WorkDao;
import cz.cvut.kbss.ear.carservice.dto.WorkDto;
import cz.cvut.kbss.ear.carservice.exceptions.InvalidDataException;
import cz.cvut.kbss.ear.carservice.exceptions.NotFoundException;
import cz.cvut.kbss.ear.carservice.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class WorkService {

    private final WorkDao workDao;
    private final UserService userService;
    private final RepairService repairService;

    @Autowired
    public WorkService(WorkDao workDao, UserService userService, RepairService repairService) {
        this.workDao = workDao;
        this.userService = userService;
        this.repairService = repairService;
    }

    // For employees only
    @Transactional
    public void create(Work work) {
        Repair repair = work.getRepair();
        LocalDateTime start = work.getBeginWork();
        LocalDateTime end = work.getFinishWork();
        User employee = userService.getCurrentUser();

        Objects.requireNonNull(repair);
        Objects.requireNonNull(start);
        Objects.requireNonNull(employee);

        Repair actualRepair = repairService.findById(repair.getId());

        if (actualRepair == null) {
            throw new NotFoundException("Repair ID is not found");
        }

        work.setRepair(actualRepair);
        work.setEmployee(employee);

        if (end.isBefore(start)) {
            throw new InvalidDataException("Start of work must be before the end");
        }

        if (employee.getRole() != Role.EMPLOYEE) {
            throw new InvalidDataException("Work can have only User with Role employee");
        }

        List<Work> works = employee.getWorks();

        if (works != null) {
            for (Work w : works) {
                if (isOverlapping(w.getBeginWork(), w.getFinishWork(), start, end)) {
                    throw new InvalidDataException("Interval of times are overlapping: creating work start <" +
                            start + "> and end <" + end + "> intersects existing work with start <" +
                            w.getBeginWork() + "> and end <" + w.getFinishWork() + ">");
                }
            }
        }

        workDao.persist(work);
        addWork(employee, work);
    }

    @Transactional
    public void addWork(User employee, Work work) {
        Objects.requireNonNull(employee);
        Objects.requireNonNull(work);
        Repair repair = work.getRepair();

        // Repair
        if (repair.getWorks() == null) {
            repair.setWorks(new ArrayList<Work>());
        }

        repair.getWorks().add(work);

        // Employee
        if (employee.getWorks() == null) {
            employee.setWorks(new ArrayList<Work>());
        }

        List<Work> works = employee.getWorks();
        works.add(work);

        repairService.update(repair);
        userService.update(employee);
    }


    @Transactional(readOnly = true)
    public Work findById(Integer id) {
        return workDao.find(id);
    }

    @Transactional(readOnly = true)
    public List<Work> findAllByEmployeeId(Integer id) {
        User employee = userService.findUser(id);
        if (employee == null) {
            throw NotFoundException.create("Employee", id);
        }

        if (employee.getRole() != Role.EMPLOYEE) {
            throw new InvalidDataException("User with this ID is not an employee");
        }

        return workDao.findAll(employee);
    }

    public static boolean isOverlapping(LocalDateTime start1, LocalDateTime end1, LocalDateTime start2, LocalDateTime end2) {
        return !start1.isAfter(end2) && !start2.isAfter(end1);
    }
}
