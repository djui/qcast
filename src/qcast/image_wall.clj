(ns qcast.image-wall
  (:import java.awt.Color
           java.awt.image.BufferedImage
           javax.imageio.ImageIO)
  (:require [clojure.java.io :as io]))


;;; Internals

(defn- posters []
  (let [is-jpg? #(and (.isFile %) (.endsWith (.getName %) ".jpg"))]
    (->> "public/img/posters"
         io/file
         file-seq
         (filter is-jpg?))))

(defn- max-rect [n]
  (let [y (int (Math/sqrt n))
        x (int (/ n y))]
    [x y]))


;;; Interface

(defn create
  ([] (create 100 "public/img/wall.jpg"))
  ([amount output-file]
    (let [posters (take amount (posters))
          [cols rows] (max-rect (count posters))
          border 3
          width 135
          height 100
          width+ (+ width border)
          height+ (+ height border)
          black (Color. 0 0 0)
          img (BufferedImage. (* cols width+) (* rows height+) BufferedImage/TYPE_INT_RGB)
          graphics (.createGraphics img)]
      (.setPaint graphics black)
      (.fillRect graphics 0 0 (.getWidth img) (.getHeight img))
      (doseq [[i poster] (map-indexed vector posters)]
        (let [tmp-img (ImageIO/read poster)
              x-pos (* width+ (rem i cols))
              y-pos (* height+ (int (/ i cols)))]
          (.drawImage graphics tmp-img x-pos y-pos width height nil)))
      (ImageIO/write img "jpg" (io/file output-file)))))
