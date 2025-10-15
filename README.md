# What is this?
This is a library for assisting with drawing particles in a graphics-like fashion.

# Why?
Firstly, because it's funny. Secondly I needed a way to use particles in a dynamic, vanilla friendly way.

# Vanilla friendly?
This mod may be installed on the server only and will work exactly the same. However, since this mod doesn't actually do anything on its own, it depends on the implementations for that to hold true.

# Doesn't do anything
Yup, this is a true library mod. It doesn't do *anything*. It doesn't change that game in any way. No mixins, no coremods. Nothing. This just provides some utilities.

# Contributions
The initial release of this mod has very little to work with. If you want to add more complex transformations just submit a PR.
So long as the mod maintains vanilla compat I don't really care. The mod should also never rely on any loader specific functions.
This means no usages of events in neo/forge, and no whatever fabric does. The mod is just easier to port that way. Also simple.