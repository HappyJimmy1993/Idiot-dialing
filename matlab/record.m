 function y=record()
rbd=get(con_rbd,'value');
    if rbd
        delete('train/*.wav');
        n=1;%��ͷ��ʼ
    end
% n=1;    
fs=11025;           %ȡ��Ƶ��

duration=2;         %¼��ʱ��

% fprintf('Press any key to start %g seconds of recording...\n',duration);
% 
% pause;

fprintf('training...\n');

y=wavrecord(duration*fs,fs);         %duration*fs ���ܵĲ�������

% fprintf('Finished training.\n');

% str=num2str(file,n);
wavwrite(y,fs,'1.wav');
% fprintf('Press any key to play the recording...\n');
% 
% pause;
code=train('D:\Program Files\MATLAB704\work\work\train\',n);
wavplay(y,fs);