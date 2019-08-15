package coordinate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum FigureType {
  LINE(Line.SIZE, Line::new),
  TRIANGLE(Triangle.SIZE, Triangle::new),
  RECTANGLE(Rectangle.SIZE, Rectangle::new);

  private static Map<Integer, FigureCreator> figureCreators;
  private int pointCount;
  private FigureCreator figureCreator;

  FigureType(int pointCount, FigureCreator figureCreator) {
    this.pointCount = pointCount;
    this.figureCreator = figureCreator;
  }

  static {
    figureCreators = Arrays.stream(FigureType.values())
        .collect(Collectors
            .toMap(figure -> figure.pointCount, figure -> figure.figureCreator));
  }

  public static Figure from(List<Point> points) {
    int pointCount = points.size();
    if(!figureCreators.containsKey(pointCount)) {
      throw new IllegalArgumentException("유효하지 않은 도형입니다.");
    }
    return figureCreators.get(pointCount).create(points);
  }

}