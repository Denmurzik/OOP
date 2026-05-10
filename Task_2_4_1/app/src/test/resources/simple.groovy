tasks {
    task id: '2-1-1', name: 'Простые числа', maxScore: 1,
            softDeadline: '2026-03-01', hardDeadline: '2026-03-15'
}

groups {
    group('12345') {
        student nick: 'stud1', fullName: 'Студент №1', repo: 'https://example.com/s1.git'
        student nick: 'stud2', fullName: 'Студент №2', repo: 'https://example.com/s2.git'
    }
}

assign nick: 'stud1', labs: ['2-1-1']
assign nick: 'stud2', labs: ['2-1-1']

checkpoints {
    checkpoint name: 'КТ1', date: '2026-04-01'
}

settings {
    grade 5, 90
    grade 4, 70
    grade 3, 50
    grade 2, 0
    testTimeoutSeconds 30
    activityThreshold 0.5
    bonus nick: 'stud2', lab: '2-1-1', score: 1
}
