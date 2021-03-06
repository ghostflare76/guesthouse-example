DROP TABLE IF EXISTS REPORT;

CREATE TABLE REPORT  (
	ID BIGINT (20),  
	SALES DECIMAL(6) ,
	QTY INT (10),
	STAFF_NAME VARCHAR(30),
	DATE DATETIME
) ;

INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('1','1.56','10','cowboy76','2014-03-12 12:11:01');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('2','2.40','15','cowboy95','2014-03-04 13:11:35');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('3','3.50','20','cowboy77','2014-03-12 14:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('4','4.50','20','cowboy77','2014-03-12 15:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('5','5.50','20','cowboy77','2014-03-12 16:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('6','6.50','20','cowboy77','2014-03-12 17:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('7','7.50','20','cowboy77','2014-03-12 18:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('8','8.50','20','cowboy77','2014-03-12 19:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('9','9.50','20','cowboy77','2014-03-12 20:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('10','10.50','20','cowboy77','2014-03-12 21:11:57');
INSERT INTO `REPORT` (`ID`, `SALES`, `QTY`, `STAFF_NAME`, `DATE`) VALUES('11','11.50','20','cowboy77','2014-03-12 22:11:57');
