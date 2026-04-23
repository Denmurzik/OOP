package org.example.dsl

import org.example.model.Assignment
import org.example.model.Checkpoint
import org.example.model.Config
import org.example.model.Group
import org.example.model.Lab
import org.example.model.Student
import org.codehaus.groovy.control.CompilerConfiguration

import java.time.LocalDate

abstract class ConfigScript extends Script {
    Config config
    File baseDir
    Group currentGroup

    // ---- задачи ----
    def tasks(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
    }

    def task(Map args) {
        config.labs << new Lab(
                args.id as String,
                args.name as String,
                (args.maxScore ?: 0) as int,
                args.softDeadline ? LocalDate.parse(args.softDeadline as String) : null,
                args.hardDeadline ? LocalDate.parse(args.hardDeadline as String) : null
        )
    }

    // ---- группы/студенты ----
    def groups(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
    }

    def group(String name, Closure c) {
        def g = new Group(name)
        config.groups << g
        currentGroup = g
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
        currentGroup = null
    }

    def student(Map args) {
        if (currentGroup == null) throw new IllegalStateException("student должен быть внутри group")
        currentGroup.students << new Student(
                args.nick as String,
                args.fullName as String,
                args.repo as String
        )
    }

    // ---- назначения ----
    def assign(Map args) {
        config.assignments << new Assignment(args.nick as String, args.labs as List<String>)
    }

    // ---- контрольные точки ----
    def checkpoints(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
    }

    def checkpoint(Map args) {
        config.checkpoints << new Checkpoint(
                args.name as String,
                LocalDate.parse(args.date as String)
        )
    }

    // ---- настройки ----
    def settings(Closure c) {
        c.delegate = this
        c.resolveStrategy = Closure.DELEGATE_FIRST
        c()
    }

    def grade(int mark, int minPercent) {
        config.settings.gradeMinPercent[mark] = minPercent
    }

    def testTimeoutSeconds(int v) {
        config.settings.testTimeoutSeconds = v
    }

    def bonus(Map args) {
        def key = (args.nick as String) + '::' + (args.lab as String)
        config.settings.bonuses[key] = (args.score ?: 0) as int
    }

    def activityThreshold(double v) {
        config.settings.activityThreshold = v
    }

    // ---- импорт других конфигов ----
    def importConfig(String relativePath) {
        def f = new File(baseDir, relativePath)
        if (!f.exists()) throw new IllegalArgumentException("Не найден файл: " + f.absolutePath)

        def cc = new CompilerConfiguration()
        cc.scriptBaseClass = ConfigScript.name
        def shell = new GroovyShell(new Binding(), cc)
        def imported = (ConfigScript) shell.parse(f)
        imported.config = this.config
        imported.baseDir = f.parentFile
        imported.run()
    }
}
