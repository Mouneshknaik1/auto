cd /d "%~dp0"
ren "jcems_outputfile.xlsx" "jcems_outputfile_%date:/=%_%time::=%.xlsx"
ren "jcems_live_avgCalc_outputfile.xlsx" "jcems_live_avgCalc_outputfile_%date:/=%_%time::=%.xlsx"
ren "jcems_live_comparission_outputfile.xlsx" "jcems_live_comparission_outputfile_%date:/=%_%time::=%.xlsx"
exit /S