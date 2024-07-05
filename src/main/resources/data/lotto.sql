INSERT INTO LOTTO_STATUS (round, drawing_date, total_sell_amount, first_prize, first_winner_count, total_first_prize, winning_number1, winning_number2, winning_number3, winning_number4, winning_number5, winning_number6, bonus_number)
VALUES
    (1, '2002-12-07', 3681782000, 0, 0, 863604600, 10, 23, 29, 33, 37, 40, 16),
    (2, '2002-12-14', 4904274000, 2002006800, 1, 0, 9, 13, 21, 25, 32, 42, 2),
    (3, '2002-12-21', 4729342000, 2000000000, 1, 0, 11, 16, 19, 21, 27, 31, 30),
    (4, '2002-12-28', 5271464000, 0, 0, 1267147200, 14, 27, 30, 31, 40, 42, 2),
    (5, '2003-01-04', 6277102000, 0, 0, 3041094900, 16, 24, 29, 40, 41, 42, 3),
    (6, '2003-01-11', 15305356000, 6574451700, 1, 0, 14, 15, 26, 27, 40, 42, 34),
    (7, '2003-01-18', 12794890000, 0, 0, 2600913000, 2, 9, 16, 25, 26, 40, 42),
    (8, '2003-01-25', 20751450000, 0, 0, 7336896000, 8, 19, 25, 34, 37, 39, 9),
    (9, '2003-02-01', 73624020000, 0, 0, 25803852000, 2, 4, 16, 17, 36, 39, 14),
    (10, '2003-02-08', 260856392000, 6430437900, 13, 0, 9, 25, 30, 33, 41, 44, 6);


SELECT round, drawing_date, total_sell_amount, first_prize, first_winner_count, total_first_prize, winning_number1, winning_number2, winning_number3, winning_number4, winning_number5, winning_number6, bonus_number FROM LOTTO_STATUS;
SELECT round, drawing_date, total_sell_amount, first_prize, second_prize, third_prize, first_winner_count, total_first_prize, winning_number1, winning_number2, winning_number3, winning_number4, winning_number5, winning_number6, bonus_number FROM LOTTO_STATUS where round > 1000;

delete from LOTTO_STATUS where round = 1;