
disp('DistributedEA Boxplot result visualization');


NUMBER_OF_BOXPLOT = 25;
METHOD_INDEX = 6;

START_ROW_NUMBER = 1;
END_ROW_NUMBER = 50;

START_INPUT_FILE_INDEX = 0;
END_INPUT_FILE_INDEX = 2;
INPUT_FILE_NAME = '/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/result/results';

OUTPUT_FILE_NAME = '/afs/ms/u/b/balcs7am/BIG/workspace/distributedea/visualization/visualizationBoxplot.jpg';




% array of method(agent names) for graph description
Legend=[];

% matrix of Input values
ArrayOfMatrices=[];

% reads all files with results to Legend and aray of Matrices
for fileNumberI=START_INPUT_FILE_INDEX:END_INPUT_FILE_INDEX

    fileName = strcat(INPUT_FILE_NAME, num2str(fileNumberI));
    fileNameWithSuffix = strcat(fileName, '.txt');

    [LegendI, MatrixInputI] = stat(fileNameWithSuffix);

    SubmatrixI = MatrixInputI(START_ROW_NUMBER:END_ROW_NUMBER,:);

    Legend = LegendI;
    ArrayOfMatrices(:,:,fileNumberI+1) = SubmatrixI;
end



VectorMethodI = [];

numberOfFiles = END_INPUT_FILE_INDEX -START_INPUT_FILE_INDEX +1

for matrixIndex=1:numberOfFiles

    % matrix of Transformed values
    MatrixTransformedI = ArrayOfMatrices(:,:,matrixIndex);

    rowMCount = size(MatrixTransformedI, 1);

    VectorMethodIJ = MatrixTransformedI(:,METHOD_INDEX);
    VectorMethodI = horzcat(VectorMethodI, VectorMethodIJ);
end

boxSize = rowMCount / NUMBER_OF_BOXPLOT;

MatrixMethodI = reshape(VectorMethodI, [boxSize*numberOfFiles,NUMBER_OF_BOXPLOT]);


h = figure
hold on

%MatrixMethodI = randn(100,25);
disp(MatrixMethodI);

subplot(2,1,1)
boxplot(MatrixMethodI)

subplot(2,1,2)
boxplot(MatrixMethodI,'PlotStyle','compact')

title('Fitness TSP ostrovů v závislosti na čase')

xlabel('x: čas v sekundách', 'FontSize', 10);
ylabel('y: hodnota fitnes v kilometrech', 'FontSize', 10);


hold off

saveas(h, OUTPUT_FILE_NAME);
