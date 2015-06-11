function y2 = filter2(filter2dir,m)
for i=1:m;
file=sprintf('%s%d.wav', filter2dir,i); 
disp(file);
amp=50;
sound=wavread('nihao.wav');
indd2=200:1800;
xx(indd2)=zeros(size(indd2));
xden=ifft(xx);
xden=real(xden);
xden=abs(xden);
h=length(sound);
thr=sqrt(2*log(h*log(h)/log(2)));
y2=wden(sound,'heursure','s','one',3,'sym8');