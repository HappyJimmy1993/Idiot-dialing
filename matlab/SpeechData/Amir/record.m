for i=1:20
fs=16000; %ȡ��Ƶ��
duration=1; %¼��ʱ��
fprintf('Press any key to start %g seconds of recording...\n',duration);
pause;
fprintf('Recording...\n');
x1=wavrecord(duration*fs,fs,'int16'); %duration*fs ���ܵĲ�������
fprintf('Finished recording.\n');
fname = sprintf('5_%d.wav',i);
wavwrite(x1,fs,fname);  
end