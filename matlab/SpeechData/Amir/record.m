for i=1:20
fs=16000; %取样频率
duration=1; %录音时间
fprintf('Press any key to start %g seconds of recording...\n',duration);
pause;
fprintf('Recording...\n');
x1=wavrecord(duration*fs,fs,'int16'); %duration*fs 是总的采样点数
fprintf('Finished recording.\n');
fname = sprintf('5_%d.wav',i);
wavwrite(x1,fs,fname);  
end