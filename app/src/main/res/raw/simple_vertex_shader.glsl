/**
顶点着色器
*/
attribute vec4 a_Position;
void main(){
    gl_Position = a_Position;
    // 指定点的大小
    glz_PointSize =10.0;
}