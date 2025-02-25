<h1 align="left">Projeto de Conclusão de Curso - IFAM</h1>

###

<p align="left">Para concluir o curso técnico em infomática no IFAM, eu juntamente com 2 colegas, implementamos um robô móvel autônomo, que emprega a técnica de navegação de campos potenciais artificiais integrados com visão computacional, através de marcadores ArUco.</p>

###

<h2 align="left">Em Funcionamento</h2>


https://github.com/user-attachments/assets/254e892f-8a2b-4306-8f47-df79b7791cb0


###



<p align="left">O sistema funciona a partir dos seguinte passos:<br><br>1️⃣. Captura da Imagem: O aplicativo móvel desenvolvido no Android Studio envia uma requisição para uma câmera posicionada verticalmente acima do ambiente. A câmera captura a imagem e a retorna ao aplicativo para processamento.<br><br>2️⃣. Identificação de Marcadores Aruco: O sistema no aplicativo processa a imagem para reconhecer marcadores Aruco, que são usados para identificar tanto o robô quanto os obstáculos no ambiente. Cada marcador possui uma posição e orientação específicas.<br><br>3️⃣. Definição do Ponto-Alvo: Com a imagem obtida, o usuário pode definir o ponto-alvo com um simples toque em qualquer ponto da foto.<br><br>4️⃣. Cálculo dos Campos Potenciais Artificiais: Com base nas posições e orientações dos marcadores Aruco identificados, são calculados os campos potenciais artificiais. Estes campos indicam a direção e a distância em linha reta que o robô deve percorrer para alcançar o ponto-alvo, levando em conta as repulsões dos obstáculos e as atrações para o ponto-alvo.<br><br>5️⃣. Movimentação do Robô: O robô se move em pequenos passos na direção determinada pelo campo potencial. Após cada movimento, ele solicita uma nova foto para atualizar suas coordenadas e corrigir sua trajetória com base nas novas posições dos marcadores Aruco.<br><br>6️⃣. Iteração até o Objetivo: Esse processo de captura de imagem, cálculo dos campos potenciais, movimento e atualização de coordenadas é repetido até que o robô alcance o ponto-alvo definido pelo usuário.</p>

![DIAGRAMA PCCT](https://github.com/user-attachments/assets/b3acfc07-29f7-4698-9b68-4a1dd8992739)

### 


<h2 align="left">Tecnologias Utilizadas</h2>

###

<p align="left">Programação: Java e C.<br>Bibliotecas e FrameWorks: OpenCV (ArUco).<br>Hardware: Arduino, ESP32, Motores, Sensores,  Placa de Circuito Impresso (PCI).<br>Ferramentas de Projeto: Git, Trello, Tinkercad, Arduino IDE, Android Studio, Matlab, Ultimaker Cura.</p>

###

<div align="left">
  <img src="https://skillicons.dev/icons?i=py" height="40" alt="python logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/opencv/opencv-original.svg" height="40" alt="opencv logo"  />
  <img width="12" />
  <img src="https://skillicons.dev/icons?i=arduino" height="40" alt="arduino logo"  />
  <img width="12" />
  <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/trello/trello-plain.svg" height="40" alt="trello logo"  />
</div>

###

<p align="left"></p>

###

<a href="https://drive.google.com/file/d/1CjR7sWFABADxk1tcqYhgFa2_Rr-Y0xR4/view?usp=sharing" target="_blank">🔗 Documentação Completa</a>

<h2 align="left">Equipe</h2>
<a href="https://github.com/Guispf950" target="_blank">🔗Guilherme da Silva Pereira</a>
<a href="https://github.com/gmonteiro08" target="_blank">🔗Guilherme Monteiro da Silva</a>

###
