function code = train(traindir, n)  %���ģ�n Ϊ������ѵ���ڼ���
                             %codeΪ��ѵ���õ����飻ѵ��ʱͬʱ�õ���mfcc.m��vqlb.m;
% Speaker Recognition: Training Stage %ѵ���׶�
%
% Input:
%       traindir : string name of directory contains all train sound files
%                  ���������ļ���
%       n        : number of train files in traindir ��testdir�в����ļ���)
%
% Output:
%       code     : trained VQ codebooks, code{i} for i-th speaker
%
% Note:
%       Sound files in traindir is supposed to be: 
%                       s1.wav, s2.wav, ..., sn.wav
% Example:
%       >> code = train('C:\data\train\', 8);

k = 16;                         % number of centroids required

 for i = 1:n                      % train a VQ codebook for each speaker
         
file=sprintf('%s%d.wav', traindir,i);   %%s%d.wav����ʲô������������������������sprintf��ɵ���ʲô���ܣ����������������������иĶ���\\\\\
disp(file);
    % �����洢·����ʾ
      
    [s, fs] = wavread(file);
    
    v = mfcc(s, fs);            % Compute MFCC's
   
    code{i} = vqlbg(v, k);      % Train VQ codebook�������飻����ù���������������
 end
