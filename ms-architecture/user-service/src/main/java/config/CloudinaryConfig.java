/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

/**
 *
 * @author Carlo
 */
public class CloudinaryConfig {
    private static final Cloudinary cloudinary = new Cloudinary(ObjectUtils.asMap(
        "cloud_name", ""+Config.get("CLOUD_NAME"),
        "api_key", ""+Config.get("API_KEY"),
        "api_secret", ""+Config.get("API_SECRET")
    ));

    public static Cloudinary getCloudinary() {
        return cloudinary;
    }
}
