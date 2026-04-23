// Главный конфиг. Подключает долгоживущие блоки и описывает текущие назначения.
importConfig 'tasks.groovy'
importConfig 'groups.groovy'
importConfig 'checkpoints.groovy'

// Назначения на проверку (самое часто меняющееся).
assign nick: 'Denmurzik', labs: ['2-1-1', '2-2-1']

settings {
    grade 5, 90
    grade 4, 70
    grade 3, 50
    grade 2, 0

    testTimeoutSeconds 180
    activityThreshold 0.5
}
