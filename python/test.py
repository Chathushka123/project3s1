from PIL import Image

def is_red(r, g, b):
    return (r >= 40 and r <= 255) and (g >= 0 and g <= 85) and (b >= 0 and b <= 85)

def shift_to_pink(r, g, b):
    b = b + 40
    return r, g, b

def change_color(image, in_map, out_map):
    for x in range(image.size[0]):
        for y in range(image.size[1]):
            r, g, b = in_map[x, y]
            if is_red(r, g, b):
                r, g, b = shift_to_pink(r, g, b)
                out_map[x, y] = (r, g, b)
            else:
                out_map[x, y] = in_map[x, y]

if __name__ == '__main__':
    in_image = Image.open('lena_std.tif')
    in_map = in_image.load()
    out_image = Image.new(in_image.mode, in_image.size)
    out_map = out_image.load()

    change_color(out_image, in_map, out_map)
    in_image.show() 
    out_image.show()
