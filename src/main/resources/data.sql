INSERT INTO training_type (trainingtype_id, training_type) VALUES
    (1, 'yoga'),
    (2, 'fitness'),
	(3, 'Zumba'),
	(4, 'stretching'),
	(5, 'resistance');

INSERT INTO gym_user (gym_user_id, firstName, lastName, username, user_password, isActive) VALUES
    (1, 'Ward', 'Mejia', 'Ward.Mejia', '8155945829', true),
    (2, 'Kathleen', 'Carr', 'Kathleen.Carr', '7545019305', true),
	(3, 'Coleman', 'Yates', 'Coleman.Yates', '4415125129', true),
	(4, 'Frazie', 'Richards', 'Frazier.Richards', '4946535140', true),
	(5, 'Myrna', 'Estrada', 'Myrna.Estrada', '5952019021', true),
	(6, 'Dave', 'Batista', 'Dave.Batista', '1234567895', true),
	(7, 'Igor', 'Gura', 'Igor.Gura', '1237767895', true),
	(8, 'Allyson', 'Bauer', 'Allyson.Bauer', '1753706703', true),
	(9, 'Mari', 'Doyle', 'Mari.Doyle', '1753799703', true),
	(10, 'Shannon', 'Velazquez', 'Shannon.Velazquez', '1788799703', true);	

INSERT INTO trainer (trainer_id, specialization_id, gym_user_id) VALUES
	(1, 1, 1),
	(2, 2, 2),
	(3, 3, 3),
	(4, 4, 4),
	(5, 5, 5);

INSERT INTO trainee (trainee_id, dateOfBirth, address, gym_user_id) VALUES
	(1, '2008-08-06', '467 Boerum Street, Bedias, Mississippi, 8314', 6),
	(2, '2002-06-11', '745 Senator Street, Abiquiu, Northern Mariana Islands, 5722', 7),
	(3, '1983-11-1', '491 Crosby Avenue, Marenisco, Arkansas, 2771', 8),
	(4, '1988-12-02', '139 Micieli Place, Corriganville, Florida, 3783', 9),
	(5, '1999-10-05', '933 Dewey Place, Northchase, District Of Columbia, 1061', 10);

INSERT INTO trainee_trainer (trainee_id, trainer_id) VALUES
	(1, 2),
	(2, 5),
	(3, 3),
	(4, 1),
	(5, 4),
	(5, 3),
	(5, 1);

INSERT INTO Training (training_id, trainee_id, trainer_id, trainingName, trainingType_id, trainingDay, trainingDuration) VALUES
	(1, 1, 2, 'aerobics', 2, '2024-03-21', 150),
	(2, 2, 5, 'pilates', 5, '2024-12-28', 90),
	(3, 3, 3, 'Zumba step', 3, '2024-08-27', 60),
	(4, 4, 1, 'vinyasa', 1, '2024-09-30', 150),
	(5, 5, 4, 'dynamic stretching', 4, '2024-11-07', 60),
	(6, 5, 3, 'Zumba step', 3, '2024-10-27', 60),
	(7, 5, 1, 'vinyasa', 1, '2024-11-21', 120),
	(8, 5, 1, 'vinyasa', 1, '2024-11-22', 120);