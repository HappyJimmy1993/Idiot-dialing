function varargout = R(varargin)
% R MATLAB code for R.fig
%      R, by itself, creates a new R or raises the existing
%      singleton*.
%
%      H = R returns the handle to a new R or the handle to
%      the existing singleton*.
%
%      R('CALLBACK',hObject,eventData,handles,...) calls the local
%      function named CALLBACK in R.M with the given input arguments.
%
%      R('Property','Value',...) creates a new R or raises the
%      existing singleton*.  Starting from the left, property value pairs are
%      applied to the GUI before R_OpeningFcn gets called.  An
%      unrecognized property name or invalid value makes property application
%      stop.  All inputs are passed to R_OpeningFcn via varargin.
%
%      *See GUI Options on GUIDE's Tools menu.  Choose "GUI allows only one
%      instance to run (singleton)".
%
% See also: GUIDE, GUIDATA, GUIHANDLES

% Edit the above text to modify the response to help R

% Last Modified by GUIDE v2.5 17-Oct-2014 19:35:33

% Begin initialization code - DO NOT EDIT
gui_Singleton = 1;
gui_State = struct('gui_Name',       mfilename, ...
                   'gui_Singleton',  gui_Singleton, ...
                   'gui_OpeningFcn', @R_OpeningFcn, ...
                   'gui_OutputFcn',  @R_OutputFcn, ...
                   'gui_LayoutFcn',  [] , ...
                   'gui_Callback',   []);
if nargin && ischar(varargin{1})
    gui_State.gui_Callback = str2func(varargin{1});
end

if nargout
    [varargout{1:nargout}] = gui_mainfcn(gui_State, varargin{:});
else
    gui_mainfcn(gui_State, varargin{:});
end
% End initialization code - DO NOT EDIT


% --- Executes just before R is made visible.
function R_OpeningFcn(hObject, eventdata, handles, varargin)
% This function has no output args, see OutputFcn.
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)
% varargin   command line arguments to R (see VARARGIN)

% Choose default command line output for R
handles.output = hObject;

% Update handles structure
guidata(hObject, handles);

% UIWAIT makes R wait for user response (see UIRESUME)
% uiwait(handles.figure1);


% --- Outputs from this function are returned to the command line.
function varargout = R_OutputFcn(hObject, eventdata, handles) 
% varargout  cell array for returning output args (see VARARGOUT);
% hObject    handle to figure
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Get default command line output from handles structure
varargout{1} = handles.output;


% --- Executes on button press in pushbutton1.
function pushbutton1_Callback(hObject, eventdata, handles)
% hObject    handle to pushbutton1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)


ncoeff = 13;          %Required number of mfcc coefficients
N = 20;               %Number of words in vocabulary
k = 3;                %Number of nearest neighbors to choose
fs=16000;             %Sampling rate 
duration1 = 0.15;     %Initial silence duration in seconds
duration2 = 2;        %Recording duration in seconds
G=2;                  %vary this factor to compensate for amplitude variations
NSpeakers = 5;        %Number of training speakers

fprintf('Press any key to start %g seconds of speech recording...', duration2); 
%pause;
silence = wavrecord(duration1*fs, fs);
fprintf('Recording speech...'); 
speechIn = wavrecord(duration2*fs, fs); % duration*fs is the total number of sample points 

fprintf('Finished recording.\n');
fprintf('System is trying to recognize what you have spoken...\n');
speechIn1 = [silence;speechIn];                  %pads with 150 ms silence
speechIn2 = speechIn1.*G;
speechIn3 = speechIn2 - mean(speechIn2);         %DC offset elimination
speechIn = nreduce(speechIn3,fs);     %Applies spectral subtraction




rMatrix1 = mfccf(ncoeff,speechIn,fs);            %Compute test feature vector
rMatrix = CMN(rMatrix1);                         %Removes convolutional noise

Sco = DTWScores(rMatrix,N);                      %computes all DTW scores
[SortedScores,EIndex] = sort(Sco);               %Sort scores increasing
K_Vector = EIndex(1:k);                          %Gets k lowest scores
Neighbors = zeros(1,k);                          %will hold k-N neighbors

%Essentially, code below uses the index of the returned k lowest scores to
%determine their classes

for t = 1:k
    u = K_Vector(t);
    for r = 1:NSpeakers-1
        if u <= (N)
            break
        else u = u - (N);
        end
    end
    Neighbors(t) = u;
    
end

%Apply k-Nearest Neighbor rule
Nbr = Neighbors
%sortk = sort(Nbr);
[Modal,Freq] = mode(Nbr);                              %most frequent value
Word = strvcat('off','blue','craft','day','eat','go','hand','ice','love','left','glass','yes','new','buy','rich','open','say','toy','fire','seven'); 
if mean(abs(speechIn)) < 0.01
    set(handles.edit1,'string','No microphone connected or you have not said anything.');
elseif ((k/Freq) > 2)                                  %if no majority
    set(handles.edit1,'string','The word you have said could not be properly recognised.');
else
    set(handles.edit1,'string',['    ' Word(Modal,:)]); %Prints recognized word
end




function edit1_Callback(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    structure with handles and user data (see GUIDATA)

% Hints: get(hObject,'String') returns contents of edit1 as text
%        str2double(get(hObject,'String')) returns contents of edit1 as a double


% --- Executes during object creation, after setting all properties.
function edit1_CreateFcn(hObject, eventdata, handles)
% hObject    handle to edit1 (see GCBO)
% eventdata  reserved - to be defined in a future version of MATLAB
% handles    empty - handles not created until after all CreateFcns called

% Hint: edit controls usually have a white background on Windows.
%       See ISPC and COMPUTER.
if ispc && isequal(get(hObject,'BackgroundColor'), get(0,'defaultUicontrolBackgroundColor'))
    set(hObject,'BackgroundColor','white');
end
